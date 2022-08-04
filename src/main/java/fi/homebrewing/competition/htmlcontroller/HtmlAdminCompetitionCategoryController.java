package fi.homebrewing.competition.htmlcontroller;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.BeerStyleRepository;
import fi.homebrewing.competition.domain.Competition;
import fi.homebrewing.competition.domain.CompetitionCategory;
import fi.homebrewing.competition.domain.CompetitionCategoryRepository;
import fi.homebrewing.competition.domain.CompetitionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/competition-categories")
public class HtmlAdminCompetitionCategoryController extends ThymeLeafController {
    protected static final String MODEL_ATTRIBUTE_SINGLE = "competitionCategory";
    protected static final String MODEL_ATTRIBUTE_MULTIPLE = "competitionCategories";

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
                          @Param("competition") Competition competition) {

        final Map<String, ?> modelAttributes = Map.of(
            HtmlAdminCompetitionController.MODEL_ATTRIBUTE_MULTIPLE,
            competitionCategoryRepository.findAll((Competition)null)
                .stream()
                .map(CompetitionCategory::getCompetition)
                .distinct()
                .toList(),
            HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE,
            competition,
            MODEL_ATTRIBUTE_MULTIPLE,
            competitionCategoryRepository.findAll(competition)
        );

        return getRowsList(model, modelAttributes);
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String getCompetitionCategoryForm(@PathVariable("id") Optional<String> oId, Model model) {
        final Map<String, ?> modelAttributes = Map.of(
            HtmlAdminCompetitionController.MODEL_ATTRIBUTE_MULTIPLE,
            competitionRepository.findAllByTypeOrderByName((Competition.Type)null),
            HtmlAdminBeerStyleController.MODEL_ATTRIBUTE_MULTIPLE,
            beerStyleRepository.findAll() // TODO: Sort needed
        );

        return getRowForm(oId, competitionCategoryRepository, model, modelAttributes, CompetitionCategory::new);
    }

    @PostMapping(value = {"/upsert", "/upsert/{id}"})
    public String upsert(@PathVariable("id") Optional<String> oId, @Valid CompetitionCategory competitionCategory, BindingResult result, Model model) {
        oId.ifPresent(competitionCategory::setId);

        final Supplier<Map<String, ?>> modelAttributes = () -> Map.of(
            HtmlAdminCompetitionController.MODEL_ATTRIBUTE_MULTIPLE,
            competitionRepository.findAllByOrderByName(),
            HtmlAdminBeerStyleController.MODEL_ATTRIBUTE_MULTIPLE,
            beerStyleRepository.findAll() // TODO: Sort needed
        );

        return upsertRow(competitionCategory, result, model, modelAttributes, competitionCategoryRepository);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        return deleteRow(id, competitionCategoryRepository);
    }

    @Override
    public String getTemplateList() {
        return "competition-category-list";
    }

    @Override
    public String getTemplateForm() {
        return "competition-category-form";
    }

    @Override
    public String getActivePage() {
        return "/admin/competition-categories/";
    }

    @Override
    public String getSingleModelName() {
        return MODEL_ATTRIBUTE_SINGLE;
    }
}
