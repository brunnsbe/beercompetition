package fi.homebrewing.competition.restcontroller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/competition")
public class CompetitionController {
    @Value("${competition-name: Unknown}")
    private String name;

    @GetMapping("/")
    String getName() {
        return name;
    }
}
