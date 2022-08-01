package fi.homebrewing.competition.htmlcontroller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.BeerRepository;
import fi.homebrewing.competition.domain.Competition;
import fi.homebrewing.competition.domain.CompetitionCategoryRepository;
import fi.homebrewing.competition.domain.CompetitionRepository;
import fi.homebrewing.competition.domain.CompetitorRepository;
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
public class HtmlAdminCompetitionController {
    private static final String THYMELEAF_TEMPLATE_COMPETITION_LIST = "competition-list";
    private static final String THYMELEAF_TEMPLATE_COMPETITION_FORM = "competition-form";

    private final BeerRepository beerRepository;
    private final CompetitionRepository competitionRepository;
    private final CompetitionCategoryRepository competitionCategoryRepository;
    private final CompetitorRepository competitorRepository;

    public HtmlAdminCompetitionController(BeerRepository beerRepository,
                                          CompetitionRepository competitionRepository,
                                          CompetitionCategoryRepository competitionCategoryRepository,
                                          CompetitorRepository competitorRepository) {
        this.beerRepository = beerRepository;
        this.competitionRepository = competitionRepository;
        this.competitionCategoryRepository = competitionCategoryRepository;
        this.competitorRepository = competitorRepository;
    }

    @GetMapping("/")
    public String getCompetitionsList(Model model,
                                      @Nullable @Param("competitionType") Competition.Type competitionType) {

        model.addAttribute("activePage", "/admin/competitions");

        // Filters
        model.addAttribute(
            "competitionTypes",
            competitionRepository.findAll().stream().map(Competition::getType).distinct().sorted().toList()
        );
        model.addAttribute("competitionType", competitionType);

        // Table
        model.addAttribute(
"competitions",
            competitionRepository.getCompetitionsSortedByName(competitionType)
        );

        return THYMELEAF_TEMPLATE_COMPETITION_LIST;
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String getCompetitionForm(@PathVariable("id") Optional<UUID> oId, Model model) {
        model.addAttribute("activePage", "/admin/competitions");

        final Competition competition = oId.map(id -> {
            return competitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid beer Id:" + id));
        }).orElseGet(Competition::new);

        model.addAttribute("competition", competition);

        return THYMELEAF_TEMPLATE_COMPETITION_FORM;
    }

    @PostMapping(value = {"/upsert", "/upsert/{id}"})
    public String upsertCompetition(@PathVariable("id") Optional<UUID> oId, @Valid Competition competition, BindingResult result) {
        oId.ifPresent(competition::setId);

        if (result.hasErrors()) {
            return THYMELEAF_TEMPLATE_COMPETITION_FORM;
        }

        competitionRepository.save(competition);
        return "redirect:/admin/competitions/";
    }

    @GetMapping("/delete/{id}")
    public String deleteCompetition(@PathVariable("id") UUID id) {
        Competition competition = competitionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid competition Id:" + id));

        competitionRepository.delete(competition);
        return "redirect:/admin/competitions/";
    }
}
