package fi.homebrewing.competition.htmlcontroller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.Beer;
import fi.homebrewing.competition.domain.BeerRepository;
import fi.homebrewing.competition.domain.Competition;
import fi.homebrewing.competition.domain.CompetitionCategory;
import fi.homebrewing.competition.domain.CompetitionCategoryHasBeerStyle;
import fi.homebrewing.competition.domain.CompetitionCategoryRepository;
import fi.homebrewing.competition.domain.Competitor;
import fi.homebrewing.competition.domain.CompetitorRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * TODO:
 * // Categories per competition
 * // Beer styles per category
 */

@Controller
@RequestMapping("/admin/beers")
public class HtmlAdminBeerController extends ThymeLeafController {
    protected static final String MODEL_ATTRIBUTE_SINGLE = "beer";
    protected static final String MODEL_ATTRIBUTE_MULTIPLE = "beers";

    private final BeerRepository beerRepository;
    private final CompetitionCategoryRepository competitionCategoryRepository;
    private final CompetitorRepository competitorRepository;

    public HtmlAdminBeerController(BeerRepository beerRepository,
                                   CompetitionCategoryRepository competitionCategoryRepository,
                                   CompetitorRepository competitorRepository) {

        this.beerRepository = beerRepository;
        this.competitionCategoryRepository = competitionCategoryRepository;
        this.competitorRepository = competitorRepository;
    }

    @GetMapping("/")
    public String getBeersList(Model model, @Param("competition") Competition competition) {
        final List<Beer> allBeers = beerRepository.findAll();

        // TODO: Add filter for category
        final Map<String, ?> modelAttributes = Map.of(
            // Filters
            HtmlAdminCompetitionController.MODEL_ATTRIBUTE_MULTIPLE,
            allBeers.stream()
                .map(Beer::getCompetitionCategoryHasBeerStyle)
                .map(CompetitionCategoryHasBeerStyle::getCompetitionCategory)
                .map(CompetitionCategory::getCompetition)
                .distinct()
                .sorted(Comparator.comparing(Competition::getName))
                .toList(),
            HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE,
            competition,
            // Table
            MODEL_ATTRIBUTE_MULTIPLE,
            allBeers.stream()
                .filter(v -> competition.getId() == null || competition.equals(v.getCompetitionCategoryHasBeerStyle().getCompetitionCategory().getCompetition()))
                .toList()
        );

        return getRowsList(model, modelAttributes);
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String getRowForm(@PathVariable("id") Optional<String> oId, Model model) {
        final Map<String, ?> modelAttributes = Map.of(
            "competitionCategories",
            competitionCategoryRepository.findAll((Competition)null),
            "competitors",
            getCompetitorsSortedByFullname()
        );

        return getRowForm(oId, beerRepository, model, modelAttributes, Beer::new);
    }

    @PostMapping(value = {"/upsert", "/upsert/{id}"})
    public String upsertBeer(@PathVariable("id") Optional<String> oId, @Valid Beer beer, BindingResult result, Model model) {
        oId.ifPresent(beer::setId);

        final Supplier<Map<String, ?>> modelAttributes = () -> Map.of(
            HtmlAdminCompetitionCategoryController.MODEL_ATTRIBUTE_MULTIPLE,
            competitionCategoryRepository.findAll((Competition)null),
            HtmlAdminCompetitionController.MODEL_ATTRIBUTE_MULTIPLE,
            getCompetitorsSortedByFullname()
        );

        return upsertRow(beer, result, model, modelAttributes, beerRepository);
    }

    @GetMapping("/delete/{id}")
    public String deleteBeer(@PathVariable("id") String id) {
        return deleteRow(id, beerRepository);
    }

    private List<Competitor> getCompetitorsSortedByFullname() {
        final List<Competitor> competitors = competitorRepository.findAll();
        competitors.sort(Comparator.comparing(Competitor::getFullName));
        return competitors;
    }

    @Override
    public String getTemplateList() {
        return "beer-list";
    }

    @Override
    public String getTemplateForm() {
        return "beer-form";
    }

    @Override
    public String getActivePage() {
        return "/admin/beers/";
    }

    @Override
    public String getSingleModelName() {
        return MODEL_ATTRIBUTE_SINGLE;
    }
}
