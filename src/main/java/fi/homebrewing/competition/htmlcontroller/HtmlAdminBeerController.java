package fi.homebrewing.competition.htmlcontroller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.Beer;
import fi.homebrewing.competition.domain.BeerRepository;
import fi.homebrewing.competition.domain.Competition;
import fi.homebrewing.competition.domain.CompetitionCategoryHasBeerStyleRepository;
import fi.homebrewing.competition.domain.CompetitionCategoryRepository;
import fi.homebrewing.competition.domain.CompetitionRepository;
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
public class HtmlAdminBeerController extends HtmlAdminController {
    protected static final String MODEL_ATTRIBUTE_SINGLE = "beer";
    protected static final String MODEL_ATTRIBUTE_MULTIPLE = "beers";

    private final BeerRepository beerRepository;
    private final CompetitionCategoryHasBeerStyleRepository competitionCategoryHasBeerStyleRepository;
    private final CompetitorRepository competitorRepository;
    private final CompetitionCategoryRepository competitionCategoryRepository;
    private final CompetitionRepository competitionRepository;

    public HtmlAdminBeerController(BeerRepository beerRepository,
                                   CompetitionCategoryHasBeerStyleRepository competitionCategoryHasBeerStyleRepository,
                                   CompetitorRepository competitorRepository,
                                   CompetitionCategoryRepository competitionCategoryRepository,
                                   CompetitionRepository competitionRepository) {

        this.beerRepository = beerRepository;
        this.competitionCategoryHasBeerStyleRepository = competitionCategoryHasBeerStyleRepository;
        this.competitorRepository = competitorRepository;
        this.competitionCategoryRepository = competitionCategoryRepository;
        this.competitionRepository = competitionRepository;
    }

    @GetMapping("/")
    public String getBeersList(Model model, @Param("competition") Competition competition) {
        final List<Beer> allBeers = beerRepository.findAll();

        // TODO: Add filter for category
        final Map<String, ?> modelAttributes = Map.of(
            // Filters
            HtmlAdminCompetitionController.MODEL_ATTRIBUTE_MULTIPLE,
            competitionRepository.findAllByCompetitionCategoryIsNotNullOrderByName(),
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
        return getRowForm(oId, beerRepository, model, getFormModelAttributes(), Beer::new);
    }

    @PostMapping(value = {"/upsert", "/upsert/{id}"})
    public String upsertBeer(@PathVariable("id") Optional<String> oId, @Valid Beer beer, BindingResult result, Model model) {
        oId.ifPresent(beer::setId);
        return upsertRow(beer, result, model, this::getFormModelAttributes, beerRepository);
    }

    @Override
    protected Map<String, ?> getFormModelAttributes() {
     return Map.of(
         "competitionCategoryHasBeerStyles",
         competitionCategoryHasBeerStyleRepository.findAll(),
         HtmlAdminCompetitorController.MODEL_ATTRIBUTE_MULTIPLE,
         competitorRepository.findAllByOrderByLastNameAscFirstNameAsc()
     );
    }

    @GetMapping("/delete/{id}")
    public String deleteBeer(@PathVariable("id") String id) {
        return deleteRow(id, beerRepository);
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
