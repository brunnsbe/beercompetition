package fi.homebrewing.competition.htmlcontroller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.BeerStyle;
import fi.homebrewing.competition.domain.BeerStyleRepository;
import fi.homebrewing.competition.domain.Competition;
import fi.homebrewing.competition.domain.CompetitionCategory;
import fi.homebrewing.competition.domain.CompetitionCategoryRepository;
import fi.homebrewing.competition.domain.CompetitionRepository;
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
@RequestMapping("/admin/competition-categories")
public class HtmlAdminCompetitionCategoryController {
    private static final String THYMELEAF_TEMPLATE_LIST = "competition-category-list";
    private static final String THYMELEAF_TEMPLATE_FORM = "competition-category-form";

    private static final String ACTIVE_PAGE = "/admin/competition-categories/";


    private final CompetitionCategoryRepository competitionCategoryRepository;
    private final CompetitionRepository competitionRepository;
    private final BeerStyleRepository beerStyleRepository;


    public HtmlAdminCompetitionCategoryController(CompetitionCategoryRepository competitionCategoryRepository,
                                                  CompetitionRepository competitionRepository,
                                                  BeerStyleRepository beerStyleRepository) {

        this.competitionCategoryRepository = competitionCategoryRepository;
        this.competitionRepository = competitionRepository;
        this.beerStyleRepository = beerStyleRepository;
    }

    @GetMapping("/")
    public String getList(Model model,
                          @Nullable @Param("competition") Competition competition) {

        model.addAttribute("activePage", ACTIVE_PAGE);

        final List<CompetitionCategory> competitionCategories = competitionCategoryRepository.findAll(competition);

        // Filters
        model.addAttribute(
            "competitions",
            competitionCategoryRepository.findAll((Competition)null)
                .stream()
                .map(CompetitionCategory::getCompetition)
                .distinct()
                .toList()
        );
        model.addAttribute("competition", competition);

        // Table
        model.addAttribute("competitionCategories", competitionCategories);

        return THYMELEAF_TEMPLATE_LIST;
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String getForm(@PathVariable("id") Optional<UUID> oId, Model model) {
        model.addAttribute("activePage", ACTIVE_PAGE);

        final CompetitionCategory competitionCategory = oId.map(id -> {
            return competitionCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        }).orElseGet(CompetitionCategory::new);

        model.addAttribute("competitionCategory", competitionCategory);

        return THYMELEAF_TEMPLATE_FORM;
    }

    @PostMapping(value = {"/upsert", "/upsert/{id}"})
    public String upsert(@PathVariable("id") Optional<UUID> oId, @Valid CompetitionCategory competitionCategory, BindingResult result) {
        oId.ifPresent(competitionCategory::setId);

        if (result.hasErrors()) {
            return THYMELEAF_TEMPLATE_FORM;
        }

        competitionCategoryRepository.save(competitionCategory);
        return "redirect:" + ACTIVE_PAGE;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") UUID id) {
        CompetitionCategory competitionCategory = competitionCategoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));

        competitionCategoryRepository.delete(competitionCategory);
        return "redirect:" + ACTIVE_PAGE;
    }
}
