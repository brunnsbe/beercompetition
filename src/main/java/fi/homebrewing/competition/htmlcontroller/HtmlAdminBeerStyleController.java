package fi.homebrewing.competition.htmlcontroller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.BeerStyle;
import fi.homebrewing.competition.domain.BeerStyleRepository;
import fi.homebrewing.competition.domain.CompetitionCategory;
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

    public HtmlAdminBeerStyleController(BeerStyleRepository beerStyleRepository) {

        this.beerStyleRepository = beerStyleRepository;
    }

    @GetMapping("/")
    public String getBeerStylesList(Model model,
                                    @Param("competitionCategory") CompetitionCategory competitionCategory) {

        final List<BeerStyle> beerStyles = beerStyleRepository.findAll(); // TODO: Add filtering by competition category

        final Map<String, ?> modelAttributes = Map.of(
            HtmlAdminCompetitionCategoryController.MODEL_ATTRIBUTE_MULTIPLE,
            List.of(), //beerStyles.stream().flatMap(v -> v.getCompetitionCategories().stream()).distinct().sorted().toList(),
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
