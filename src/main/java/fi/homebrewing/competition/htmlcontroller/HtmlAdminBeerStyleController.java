package fi.homebrewing.competition.htmlcontroller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.BeerStyle;
import fi.homebrewing.competition.domain.BeerStyleRepository;
import fi.homebrewing.competition.domain.CompetitionCategory;
import fi.homebrewing.competition.domain.CompetitionCategoryRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/beer-styles")
public class HtmlAdminBeerStyleController extends ThymeLeafController {
    protected static final String MODEL_ATTRIBUTE_SINGLE = "beerStyle";
    protected static final String MODEL_ATTRIBUTE_MULTIPLE = "beerStyles";

    private final BeerStyleRepository beerStyleRepository;
    private final CompetitionCategoryRepository competitionCategoryRepository;

    public HtmlAdminBeerStyleController(BeerStyleRepository beerStyleRepository,
                                        CompetitionCategoryRepository competitionCategoryRepository) {

        this.beerStyleRepository = beerStyleRepository;
        this.competitionCategoryRepository = competitionCategoryRepository;
    }

    @GetMapping("/")
    public String getBeerStylesList(Model model,
                                    @Param("competitionCategory") CompetitionCategory competitionCategory) {

        final List<BeerStyle> beerStyles = competitionCategory.getId() == null
            ? beerStyleRepository.findAllByOrderByName()
            : beerStyleRepository.findAllByCompetitionCategoriesInOrderByNameAsc(Set.of(competitionCategory));

        final Map<String, ?> modelAttributes = Map.of(
            HtmlAdminCompetitionCategoryController.MODEL_ATTRIBUTE_MULTIPLE,
            competitionCategoryRepository.findDistinctByBeerStylesIsNotNullOrderByName(),
            HtmlAdminCompetitionCategoryController.MODEL_ATTRIBUTE_SINGLE,
            competitionCategory,
            MODEL_ATTRIBUTE_MULTIPLE,
            beerStyles
        );

        return getRowsList(model, modelAttributes);
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String getBeerStyleForm(@PathVariable("id") Optional<String> oId,
                                   Model model) {

        return getRowForm(oId, beerStyleRepository, model, Map.of(), BeerStyle::new);
    }

    @PostMapping(value = {"/upsert", "/upsert/{id}"})
    public String upsertBeerStyle(@PathVariable("id") Optional<String> oId,
                                  @Valid BeerStyle beerStyle,
                                  BindingResult result,
                                  Model model) {

        oId.ifPresent(beerStyle::setId);
        return upsertRow(beerStyle, result, model, Map::of, beerStyleRepository);
    }

    @GetMapping("/delete/{id}")
    public String deleteCompetition(@PathVariable("id") String id) {
        return deleteRow(id, beerStyleRepository);
    }

    @Override
    public String getTemplateList() {
        return "beer-style-list";
    }

    @Override
    public String getTemplateForm() {
        return "beer-style-form";
    }

    @Override
    public String getActivePage() {
        return "/admin/beer-styles/";
    }

    @Override
    public String getSingleModelName() {
        return MODEL_ATTRIBUTE_SINGLE;
    }
}
