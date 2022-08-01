package fi.homebrewing.competition.htmlcontroller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.Beer;
import fi.homebrewing.competition.domain.BeerRepository;
import fi.homebrewing.competition.domain.Competition;
import fi.homebrewing.competition.domain.CompetitionCategory;
import fi.homebrewing.competition.domain.CompetitionCategoryRepository;
import fi.homebrewing.competition.domain.CompetitionRepository;
import fi.homebrewing.competition.domain.Competitor;
import fi.homebrewing.competition.domain.CompetitorRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/beers")
public class HtmlAdminBeerController {
    private static final String THYMELEAF_TEMPLATE_BEER_LIST = "beer-list";
    private static final String THYMELEAF_TEMPLATE_BEER_FORM = "beer-form";

    private final BeerRepository beerRepository;
    private final CompetitionRepository competitionRepository;
    private final CompetitionCategoryRepository competitionCategoryRepository;
    private final CompetitorRepository competitorRepository;

    public HtmlAdminBeerController(BeerRepository beerRepository,
                                   CompetitionRepository competitionRepository,
                                   CompetitionCategoryRepository competitionCategoryRepository,
                                   CompetitorRepository competitorRepository) {
        this.beerRepository = beerRepository;
        this.competitionRepository = competitionRepository;
        this.competitionCategoryRepository = competitionCategoryRepository;
        this.competitorRepository = competitorRepository;
    }

    @GetMapping("/")
    public String getBeersList(Model model,
                               @Param("competition") Competition competition) {

        model.addAttribute("activePage", "/admin/beers");

        // Filters
        model.addAttribute(
            "competitions",
            competitionRepository.getCompetitionsSortedByName(null)
        );
        model.addAttribute("competition", competition);

        // TODO: Add filter for category

        // Table
        final List<Beer> allBeers = beerRepository.findAll();
        model.addAttribute(
            "currentBeers",
            allBeers.stream().filter(v -> competition.getId() == null || competition.equals(v.getCompetitionCategory().getCompetition())).toList()
        );

        return THYMELEAF_TEMPLATE_BEER_LIST;
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String getBeerForm(@PathVariable("id") Optional<UUID> oId, Model model) {
        model.addAttribute("activePage", "/admin/beers");

        final Beer beer = oId.map(id -> {
            return beerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid beer Id:" + id));
        }).orElseGet(Beer::new);

        model.addAttribute("beer", beer);
        model.addAttribute("competitionCategories", getCompetitionCategoriesSortedByName());
        model.addAttribute("competitors", getCompetitorsSortedByFullname());

        return THYMELEAF_TEMPLATE_BEER_FORM;
    }

    @PostMapping(value = {"/upsert", "/upsert/{id}"})
    public String upsertBeer(@PathVariable("id") Optional<UUID> oId, @Valid Beer beer, BindingResult result, Model model) {
        oId.ifPresent(beer::setId);

        if (result.hasErrors()) {
            model.addAttribute("competitionCategories", getCompetitionCategoriesSortedByName());
            model.addAttribute("competitors", getCompetitorsSortedByFullname());

            return THYMELEAF_TEMPLATE_BEER_FORM;
        }

        beerRepository.save(beer);
        return "redirect:/admin/beers/";
    }

    @GetMapping("/delete/{id}")
    public String deleteBeer(@PathVariable("id") UUID id) {
        Beer user = beerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid beer Id:" + id));

        beerRepository.delete(user);
        return "redirect:/admin/beers/";
    }

    private List<Competitor> getCompetitorsSortedByFullname() {
        final List<Competitor> competitors = competitorRepository.findAll();
        competitors.sort(Comparator.comparing(Competitor::getFullName));
        return competitors;
    }

    private List<CompetitionCategory> getCompetitionCategoriesSortedByName() {
        final List<CompetitionCategory> competitionCategories = competitionCategoryRepository.findAll();
        competitionCategories.sort(Comparator.comparing(CompetitionCategory::getName));
        return competitionCategories;
    }

    private List<Competition> getCompetitionsSortedByName() {
        final List<Competition> competitions = competitionRepository.findAll();
        competitions.sort(Comparator.comparing(Competition::getName));
        return competitions;
    }
}
