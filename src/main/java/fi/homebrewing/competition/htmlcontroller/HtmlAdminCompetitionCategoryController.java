package fi.homebrewing.competition.htmlcontroller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
public class HtmlAdminCompetitionCategoryController extends HtmlAdminController {
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
            competitionRepository.findAllByCompetitionCategoryIsNotNullOrderByName(),
            HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE,
            competition,
            MODEL_ATTRIBUTE_MULTIPLE,
            competitionCategoryRepository.findAll(competition)
        );

        return getRowsList(model, modelAttributes);
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String getCompetitionCategoryForm(@PathVariable("id") Optional<UUID> oId, Model model) {
        return getRowForm(oId, competitionCategoryRepository, model, getFormModelAttributes(), CompetitionCategory::new);
    }

    @PostMapping(value = {"/upsert", "/upsert/{id}"})
    public String upsert(@PathVariable("id") Optional<UUID> oId, @Valid CompetitionCategory competitionCategory, BindingResult result, Model model) {
        oId.ifPresent(competitionCategory::setId);

        return upsertRow(competitionCategory, result, model, this::getFormModelAttributes, competitionCategoryRepository);
    }

    @Override
    protected Map<String, ?> getFormModelAttributes() {
        return Map.of(
            HtmlAdminCompetitionController.MODEL_ATTRIBUTE_MULTIPLE,
            competitionRepository.findAllByOrderByName(),
            HtmlAdminBeerStyleController.MODEL_ATTRIBUTE_MULTIPLE,
            beerStyleRepository.findAllByOrderByName()
        );
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") UUID id) {
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
