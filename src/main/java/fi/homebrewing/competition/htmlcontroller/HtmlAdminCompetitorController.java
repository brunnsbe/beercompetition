package fi.homebrewing.competition.htmlcontroller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.Competition;
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

@Controller
@RequestMapping("/admin/competitors")
public class HtmlAdminCompetitorController extends HtmlAdminController {
    protected static final String MODEL_ATTRIBUTE_SINGLE = "competitor";
    protected static final String MODEL_ATTRIBUTE_MULTIPLE = "competitors";

    private final CompetitorRepository competitorRepository;

    public HtmlAdminCompetitorController(CompetitorRepository competitorRepository) {
        this.competitorRepository = competitorRepository;
    }

    @GetMapping("/")
    public String getCompetitorsList(Model model, @Param("competition") Competition competition) {
        final List<Competitor> allCompetitors = competitorRepository.findAll(); // TODO: Add sorting

        // TODO: Add filter for category
        final Map<String, ?> modelAttributes = Map.of(
            // Filters
            /*
            HtmlAdminCompetitionController.MODEL_ATTRIBUTE_MULTIPLE,
            allBeers.stream()
                .map(Beer::getCompetitionCategory)
                .map(CompetitionCategory::getCompetition)
                .distinct()
                .sorted(Comparator.comparing(Competition::getName))
                .toList(),
            HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE,
            competition,
             */
            // Table
            MODEL_ATTRIBUTE_MULTIPLE,
            allCompetitors.stream()
                //.filter(v -> v.competition.getId() == null || competition.equals(v.getCompetitionCategory().getCompetition()))
                .toList()
        );

        return getRowsList(model, modelAttributes);
    }

    @GetMapping(value = {"/edit/", "/edit/{id}/"})
    public String getCompetitorForm(@PathVariable("id") Optional<UUID> oId, Model model) {
        return getRowForm(oId, competitorRepository, model, getFormModelAttributes(), Competitor::new);
    }

    @PostMapping(value = {"/upsert/", "/upsert/{id}"})
    public String upsertCompetitor(@PathVariable("id") Optional<UUID> oId, @Valid Competitor competitor, BindingResult result, Model model) {
        oId.ifPresent(competitor::setId);

        return upsertRow(competitor, result, model, this::getFormModelAttributes, competitorRepository);
    }

    @Override
    protected Map<String, ?> getFormModelAttributes() {
        return Map.of();
    }

    @GetMapping("/delete/{id}/")
    public String deleteBeer(@PathVariable("id") UUID id) {
        return deleteRow(id, competitorRepository);
    }

    @Override
    public String getTemplateList() {
        return "competitor-list";
    }

    @Override
    public String getTemplateForm() {
        return "competitor-form";
    }

    @Override
    public String getActivePage() {
        return "/admin/competitors/";
    }

    @Override
    public String getSingleModelName() {
        return MODEL_ATTRIBUTE_SINGLE;
    }
}
