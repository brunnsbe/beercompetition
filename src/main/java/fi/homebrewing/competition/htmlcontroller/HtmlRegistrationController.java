package fi.homebrewing.competition.htmlcontroller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.Beer;
import fi.homebrewing.competition.domain.BeerRepository;
import fi.homebrewing.competition.domain.Competition;
import fi.homebrewing.competition.domain.CompetitionCategoryBeerStyleRepository;
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
    private final CompetitionCategoryBeerStyleRepository competitionCategoryBeerStyleRepository;

    public HtmlRegistrationController(CompetitionRepository competitionRepository,
                                      CompetitorRepository competitorRepository,
                                      BeerRepository beerRepository,
                                      CompetitionCategoryBeerStyleRepository competitionCategoryBeerStyleRepository) {

        this.competitionRepository = competitionRepository;
        this.competitorRepository = competitorRepository;
        this.beerRepository = beerRepository;
        this.competitionCategoryBeerStyleRepository = competitionCategoryBeerStyleRepository;
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
        if (oPersonId.isPresent()) {
            model.addAttribute("activePage", "/registration/competition/" + oCompetitionId.get() + "/person/" + competitor.getId() + "/beers/");
        }

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

        oPersonId.ifPresent(competitor::setId);

        if (result.hasErrors()) {
            model.addAttribute(HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE, competition);
            model.addAttribute(HtmlAdminCompetitorController.MODEL_ATTRIBUTE_SINGLE, competitor); // ??
            if (oPersonId.isPresent()) {
                model.addAttribute("activePage", "/registration/competition/" + oCompetitionId.get() + "/person/" + competitor.getId() + "/beers/");
            }

            return "competitor-form";
        }


        if (oPersonId.isEmpty()) {
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
                    .filter(v -> competition.getId() == null || competition.equals(v.getCompetitionCategoryBeerStyle().getCompetitionCategory().getCompetition()))
                    .toList()
            )
        );

        return "registration-beer-list";
    }

    @GetMapping(value = {"/{competitionId}/person/{personId}/beers/edit/", "/{competitionId}/person/{personId}/beers/edit/{beerId}"})
    public String getBeerForm(@PathVariable("competitionId") Optional<UUID> oCompetitionId,
                              @PathVariable("personId") Optional<UUID> oPersonId,
                              @PathVariable("beerId") Optional<UUID> oBeerId,
                              Model model) {

        final Competition competition = oCompetitionId.map(id -> {
            return competitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid competition id:" + id));
        }).orElseThrow(() -> new IllegalArgumentException("No competition id given"));

        final Competitor competitor = oPersonId.map(id -> {
            return competitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid competitor id:" + id));
        }).orElseThrow(() -> new IllegalArgumentException("No competitor id given"));

        final Beer beer = oBeerId.map(id -> {
            final Beer v = beerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));

            // Validation of UUIDs in URL
            if (!v.getCompetitionCategoryBeerStyle().getCompetitionCategory().getCompetition().equals(competition)) {
                throw new IllegalArgumentException("Illegal competition id: " + competition.getId());
            }
            if (!v.getCompetitor().equals(competitor)) {
                throw new IllegalArgumentException("Illegal competitor id: " + competitor.getId());
            }

            return v;
        }).orElseGet(Beer::new);

        model.addAllAttributes(
            Map.of(
                HtmlAdminBeerController.MODEL_ATTRIBUTE_SINGLE,
                beer,
                HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE,
                competition,
                "competitionCategoryBeerStyles",
                competitionCategoryBeerStyleRepository.findAllByCompetitionOrderByCategoryNameAscBeerStyleAsc(competition),
                HtmlAdminCompetitorController.MODEL_ATTRIBUTE_MULTIPLE,
                List.of(competitor),
                "activePage",
                "/registration/competition/" + oCompetitionId.get() + "/person/" + competitor.getId() + "/beers/"
            )
        );

        return "beer-form";
    }

    @PostMapping(value = {"/{competitionId}/person/{personId}/beers/upsert/", "/{competitionId}/person/{personId}/beers/upsert/{beerId}"})
    public String upsertBeer(@PathVariable("competitionId") Optional<UUID> oCompetitionId,
                             @PathVariable("personId") Optional<UUID> oPersonId,
                             @PathVariable("beerId") Optional<UUID> oBeerId,
                             @Valid Beer beer, BindingResult result, Model model) {

        oBeerId.ifPresent(beer::setId);
        // Check that the given competition in the URL really exists
        final Competition competition = oCompetitionId.map(id -> {
            return competitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid competition id:" + id));
        }).orElseThrow(() -> new IllegalArgumentException("No competition id given"));

        final Competitor competitor = oPersonId.map(id -> {
            return competitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid competitor id:" + id));
        }).orElseThrow(() -> new IllegalArgumentException("No competitor id given"));

        if (result.hasErrors()) {
            model.addAllAttributes(
                Map.of(
                    HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE,
                    competition,
                    HtmlAdminCompetitorController.MODEL_ATTRIBUTE_MULTIPLE,
                    List.of(competitor),
                    "competitionCategoryBeerStyles",
                    competitionCategoryBeerStyleRepository.findAllByCompetitionOrderByCategoryNameAscBeerStyleAsc(competition),
                    "activePage",
                    "/registration/competition/" + oCompetitionId.get() + "/person/" + competitor.getId() + "/beers/"
                )
            );

            return "beer-form";
        }

        beerRepository.save(beer);

        return "redirect:/registration/competition/" + oCompetitionId.get() + "/person/" + competitor.getId() + "/beers/";
    }

    @GetMapping(value = {"/{competitionId}/person/{personId}/beers/delete/{beerId}"})
    public String deleteBeer(@PathVariable("competitionId") Optional<UUID> oCompetitionId,
                             @PathVariable("personId") Optional<UUID> oPersonId,
                             @PathVariable("beerId") Optional<UUID> oBeerId) {

        // Check that the given competition in the URL really exists
        final Competition competition = oCompetitionId.map(id -> {
            return competitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid competition id:" + id));
        }).orElseThrow(() -> new IllegalArgumentException("No competition id given"));

        final Competitor competitor = oPersonId.map(id -> {
            return competitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid competitor id:" + id));
        }).orElseThrow(() -> new IllegalArgumentException("No competitor id given"));

        final Beer beer = oBeerId.map(id -> {
            return beerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid beer id:" + id));
        }).orElseThrow(() -> new IllegalArgumentException("No beer id given"));

        // Validation of UUIDs in URL
        if (!beer.getCompetitionCategoryBeerStyle().getCompetitionCategory().getCompetition().equals(competition)) {
            throw new IllegalArgumentException("Illegal competition id: " + competition.getId());
        }
        if (!beer.getCompetitor().equals(competitor)) {
            throw new IllegalArgumentException("Illegal competitor id: " + competitor.getId());
        }

        beerRepository.delete(beer);
        return "redirect:/registration/competition/" + oCompetitionId.get() + "/person/" + competitor.getId() + "/beers/";
    }
}
