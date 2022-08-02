package fi.homebrewing.competition.htmlcontroller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.Competition;
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
@RequestMapping("/admin/competitions")
public class HtmlAdminCompetitionController extends ThymeLeafController {
    protected static final String MODEL_ATTRIBUTE_SINGLE = "competition";
    protected static final String MODEL_ATTRIBUTE_MULTIPLE = "competitions";

    private final CompetitionRepository competitionRepository;

    public HtmlAdminCompetitionController(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    @GetMapping("/")
    public String getCompetitionsList(Model model,
                                      @Nullable @Param("competitionType") Competition.Type competitionType) {

        final Map<String, ?> modelAttributes;
        if (competitionType == null) {
            modelAttributes = Map.of(
                "competitionTypes",
                competitionRepository.findAll().stream().map(Competition::getType).distinct().sorted().toList(),
                MODEL_ATTRIBUTE_MULTIPLE,
                competitionRepository.findAll(competitionType)
            );
        } else {
            modelAttributes = Map.of(
                "competitionTypes",
                competitionRepository.findAll().stream().map(Competition::getType).distinct().sorted().toList(),
                "competitionType",
                competitionType,
                MODEL_ATTRIBUTE_MULTIPLE,
                competitionRepository.findAll(competitionType)
            );
        }

        return getRowsList(model, modelAttributes);
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String getCompetitionForm(@PathVariable("id") Optional<UUID> oId, Model model) {
        return getRowForm(oId, competitionRepository, model, Map.of(), Competition::new);
    }

    @PostMapping(value = {"/upsert", "/upsert/{id}"})
    public String upsertCompetition(@PathVariable("id") Optional<UUID> oId, @Valid Competition competition, BindingResult result, Model model) {
        oId.ifPresent(competition::setId);
        return upsertRow(competition, result, model, Map::of, competitionRepository);
    }

    @GetMapping("/delete/{id}")
    public String deleteCompetition(@PathVariable("id") UUID id) {
        return deleteRow(id, competitionRepository);
    }

    @Override
    public String getTemplateList() {
        return "competition-list";
    }

    @Override
    public String getTemplateForm() {
        return "competition-form";
    }

    @Override
    public String getActivePage() {
        return "/admin/competitions/";
    }

    @Override
    public String getSingleModelName() {
        return MODEL_ATTRIBUTE_SINGLE;
    }
}
