package fi.homebrewing.competition.htmlcontroller;

import java.util.List;

import fi.homebrewing.competition.domain.Competition;
import fi.homebrewing.competition.domain.CompetitionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ping")
public class HtmlPingController {
    private final CompetitionRepository competitionRepository;

    public HtmlPingController(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    @GetMapping(value = {"/"})
    public String getPingCheck(Model model) {
        final List<Competition> competitions = competitionRepository.findAll();

        model.addAttribute(HtmlAdminCompetitionController.MODEL_ATTRIBUTE_MULTIPLE, competitions);

        return "ping";
    }
}
