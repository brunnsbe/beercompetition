package fi.homebrewing.competition.htmlcontroller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.Beer;
import fi.homebrewing.competition.domain.BeerRepository;
import fi.homebrewing.competition.domain.Competition;
import fi.homebrewing.competition.domain.CompetitionRepository;
import fi.homebrewing.competition.domain.Competitor;
import fi.homebrewing.competition.domain.CompetitorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration/competition")
public class HtmlRegistrationController {

    private final CompetitionRepository competitionRepository;
    private final CompetitorRepository competitorRepository;
    private final BeerRepository beerRepository;

    public HtmlRegistrationController(CompetitionRepository competitionRepository,
                                      CompetitorRepository competitorRepository,
                                      BeerRepository beerRepository) {

        this.competitionRepository = competitionRepository;
        this.competitorRepository = competitorRepository;
        this.beerRepository = beerRepository;
    }

    @GetMapping(value = {"/{competitionId}"})
    public String getCompetitionRegistrationIntroduction(@PathVariable("competitionId") Optional<UUID> oCompetitionId,
                                                         Model model) {

        final Competition competition = oCompetitionId.map(id -> {
            return competitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        }).orElseThrow(() -> new IllegalArgumentException("No id given"));

        model.addAttribute(HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE, competition);

        return "registration-competition-intro";
    }

    @GetMapping(value = {"/{competitionId}/person/edit/", "/{competitionId}/person/{personId}/edit/"})
    public String getCompetitionSingleCompetitorForm(@PathVariable("competitionId") Optional<UUID> oCompetitionId,
                                                     @PathVariable("personId") Optional<UUID> oPersonId,
                                                     Model model) {

        final Competition competition = oCompetitionId.map(id -> {
            return competitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        }).orElseThrow(() -> new IllegalArgumentException("No id given"));

        model.addAttribute(HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE, competition);

        final Competitor competitor = oPersonId.map(id -> {
            return competitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        }).orElseGet(Competitor::new);

        model.addAttribute(HtmlAdminCompetitorController.MODEL_ATTRIBUTE_SINGLE, competitor);

        return "competitor-form";
    }

    @PostMapping(value = {"/{competitionId}/person/upsert", "/{competitionId}/person/upsert/{personId}"})
    public String upsertCompetitionSingleCompetitor(@PathVariable("competitionId") Optional<UUID> oCompetitionId,
                                                    @PathVariable("personId") Optional<UUID> oPersonId,
                                                    @Valid Competitor competitor,
                                                    BindingResult result,
                                                    Model model) throws IllegalAccessException {

        // Check that the given competition in the URL really exists
        final Competition competition = oCompetitionId.map(id -> {
            return competitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid competition id:" + id));
        }).orElseThrow(() -> new IllegalArgumentException("No competition id given"));

        if (result.hasErrors()) {
            model.addAttribute(HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE, competition);

            return "competitor-form";
        }

        if (oPersonId.isPresent()) {
            competitor.setId(oPersonId.get());
        } else {
            // Prevent email fishing
            final String givenEmailAddress = competitor.getEmailAddress();
            final Optional<Competitor> oCompetitorWithGivenEmailAddress = competitorRepository.findByEmailAddress(givenEmailAddress);
            if (oCompetitorWithGivenEmailAddress.isPresent()) {
                // TODO: Add email sending
                throw new IllegalAccessException("Email already exists, link sent by email");
            }
        }

        competitorRepository.save(competitor);

        // TODO: Add email sending when saved if new registration

        return "redirect:/registration/competition/" + oCompetitionId.get() + "/person/" + competitor.getId() + "/beers/";
    }

    @GetMapping(value = {"/{competitionId}/person/{personId}/beers/"})
    public String getBeersList(Model model,
                               @PathVariable("competitionId") Optional<UUID> oCompetitionId,
                               @PathVariable("personId") Optional<UUID> oPersonId) {

        final Competition competition = oCompetitionId.map(id -> {
            return competitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid competition id:" + id));
        }).orElseThrow(() -> new IllegalArgumentException("No competition id given"));

        final Competitor competitor = oPersonId.map(id -> {
            return competitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid competitor id:" + id));
        }).orElseThrow(() -> new IllegalArgumentException("No competitor id given"));

        final List<Beer> allBeers = beerRepository.findAllByCompetitorOrderByNameAsc(competitor);

        model.addAllAttributes(
            Map.of(
                HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE,
                competition,
                HtmlAdminCompetitorController.MODEL_ATTRIBUTE_SINGLE,
                competitor,
                // Table
                HtmlAdminBeerController.MODEL_ATTRIBUTE_MULTIPLE,
                allBeers.stream() // We cannot filter via competition due to no direct link between beer and competition
                    .filter(v -> competition.getId() == null || competition.equals(v.getCompetitionCategoryHasBeerStyle().getCompetitionCategory().getCompetition()))
                    .toList()
            )
        );

        return "registration-beer-list";
    }
}
