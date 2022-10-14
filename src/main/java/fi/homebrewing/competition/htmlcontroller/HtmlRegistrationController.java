package fi.homebrewing.competition.htmlcontroller;

import java.util.List;
import java.util.Locale;
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
import fi.homebrewing.competition.email.EmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final EmailSender emailSender;
    private final MessageSource messageSource;

    @Value("${SERVER_URL:http://localhost:80}")
    private String serverURL;

    public HtmlRegistrationController(CompetitionRepository competitionRepository,
                                      CompetitorRepository competitorRepository,
                                      BeerRepository beerRepository,
                                      CompetitionCategoryBeerStyleRepository competitionCategoryBeerStyleRepository,
                                      EmailSender emailSender,
                                      MessageSource messageSource) {

        this.competitionRepository = competitionRepository;
        this.competitorRepository = competitorRepository;
        this.beerRepository = beerRepository;
        this.competitionCategoryBeerStyleRepository = competitionCategoryBeerStyleRepository;
        this.emailSender = emailSender;
        this.messageSource = messageSource;
    }

    @GetMapping(value = {"/{competitionId}/"})
    public String getCompetitionRegistrationIntroduction(@PathVariable("competitionId") UUID competitionId,
                                                         Model model) {

        final Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + competitionId));

        if (competition.hasDeadlinePassed()) {
            return "registration-error-deadline-date-passed";
        }

        model.addAttribute(HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE, competition);

        return "registration-competition-intro";
    }

    @GetMapping(value = {"/{competitionId}/person/edit/", "/{competitionId}/person/{personId}/edit/"})
    public String getCompetitionSingleCompetitorForm(@PathVariable("competitionId") UUID competitionId,
                                                     @PathVariable("personId") Optional<UUID> oPersonId,
                                                     Model model) {

        final Competition competition = competitionRepository.findById(competitionId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + competitionId));
        model.addAttribute(HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE, competition);

        final Competitor competitor = oPersonId.map(id -> {
            return competitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        }).orElseGet(Competitor::new);

        model.addAttribute(HtmlAdminCompetitorController.MODEL_ATTRIBUTE_SINGLE, competitor);
        if (oPersonId.isPresent()) {
            model.addAttribute("activePage", "/registration/competition/" + competitionId + "/person/" + competitor.getId() + "/beers/");
        } else {
            model.addAttribute("activePage", "/registration/competition/" + competitionId + "/");
        }

        return "competitor-form";
    }

    @PostMapping(value = {"/{competitionId}/person/upsert", "/{competitionId}/person/upsert/{personId}"})
    public String upsertCompetitionSingleCompetitor(@PathVariable("competitionId") UUID competitionId,
                                                    @PathVariable("personId") Optional<UUID> oPersonId,
                                                    @Valid Competitor competitor,
                                                    BindingResult result,
                                                    Model model) {

        final Competition competition = competitionRepository.findById(competitionId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + competitionId));
        model.addAttribute(HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE, competition);

        oPersonId.ifPresent(competitor::setId);

        if (result.hasErrors()) {
            model.addAttribute(HtmlAdminCompetitionController.MODEL_ATTRIBUTE_SINGLE, competition);
            model.addAttribute(HtmlAdminCompetitorController.MODEL_ATTRIBUTE_SINGLE, competitor); // ??
            oPersonId.ifPresent(uuid -> model.addAttribute("activePage", "/registration/competition/" + competitionId + "/person/" + uuid + "/beers/"));

            return "competitor-form";
        }

        if (oPersonId.isEmpty()) {
            // Prevent email fishing
            final String givenEmailAddress = competitor.getEmailAddress();
            final Optional<Competitor> oCompetitorWithGivenEmailAddress = competitorRepository.findByEmailAddress(givenEmailAddress);
            if (oCompetitorWithGivenEmailAddress.isPresent()) {
                sendEmail(
                    competition,
                    oCompetitorWithGivenEmailAddress.get(),
                    "error.the_given_email_address_is_already_registered",
                    "email.body.please_use_this_link_for_registration"
                );

                return "redirect:/registration/competition/email_already_exists/";
            }
        }

        competitorRepository.save(competitor);

        sendEmail(
            competition,
            competitor,
            "email.subject.thank_you_for_registering",
            "email.body.thank_you_for_registering_here_is_your_link"
        );

        return "redirect:/registration/competition/" + competitionId + "/person/" + competitor.getId() + "/beers/";
    }

    private void sendEmail(Competition competition, Competitor competitor, String langCodeSubject, String langCodeBody) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String subject = messageSource.getMessage(langCodeSubject, null, locale)
            + " (" + competition.getName() + ")";
        final String body = messageSource.getMessage(langCodeBody, null, locale)
            + System.lineSeparator()
            + serverURL
            + "/registration/competition/" + competition.getId() + "/person/" + competitor.getId() + "/beers/";

        emailSender.sendEmail(competitor.getEmailAddress(), subject, body);
    }

    @GetMapping(value = {"/email_already_exists/"})
    public String emailAlreadyExists() {
        return "registration-error-email-already-exists";
    }

    @GetMapping(value = {"/{competitionId}/person/{personId}/beers/"})
    public String getBeersList(Model model,
                               @PathVariable("competitionId") UUID competitionId,
                               @PathVariable("personId") UUID personId) {

        final Competition competition = competitionRepository.findById(competitionId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + competitionId));
        final Competitor competitor = competitorRepository.findById(personId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + personId));

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

    @GetMapping(value = {"/{competitionId}/person/{personId}/beers/edit/", "/{competitionId}/person/{personId}/beers/edit/{beerId}/"})
    public String getBeerForm(@PathVariable("competitionId") UUID competitionId,
                              @PathVariable("personId") UUID competitorId,
                              @PathVariable("beerId") Optional<UUID> oBeerId,
                              Model model) {

        final Competition competition = competitionRepository.findById(competitionId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + competitionId));

        if (competition.hasDeadlinePassed()) {
            return "registration-error-deadline-date-passed";
        }

        final Competitor competitor = competitorRepository.findById(competitorId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + competitorId));

        final Beer beer = oBeerId.map(id -> {
            final Beer v = beerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));

            // Validation of UUIDs in URL
            if (!v.getCompetitionCategoryBeerStyle().getCompetitionCategory().getCompetition().equals(competition)) {
                throw new IllegalArgumentException("Illegal competition id: " + competitionId);
            }
            if (!v.getCompetitor().equals(competitor)) {
                throw new IllegalArgumentException("Illegal competitor id: " + competitorId);
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
                "/registration/competition/" + competitionId + "/person/" + competitorId + "/beers/"
            )
        );

        return "beer-form";
    }

    @PostMapping(value = {"/{competitionId}/person/{personId}/beers/upsert/", "/{competitionId}/person/{personId}/beers/upsert/{beerId}"})
    public String upsertBeer(@PathVariable("competitionId") UUID competitionId,
                             @PathVariable("personId") UUID competitorId,
                             @PathVariable("beerId") Optional<UUID> oBeerId,
                             @Valid Beer beer,
                             BindingResult result,
                             Model model) {

        oBeerId.ifPresent(beer::setId);

        final Competition competition = competitionRepository.findById(competitionId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + competitionId));

        if (competition.hasDeadlinePassed()) {
            return "registration-error-deadline-date-passed";
        }

        final Competitor competitor = competitorRepository.findById(competitorId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + competitorId));

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
                    "/registration/competition/" + competitionId + "/person/" + competitorId + "/beers/"
                )
            );

            return "beer-form";
        }

        // Prevent user from overwriting the current score and finalist
        oBeerId.ifPresent(id -> {
            final Beer currentBeer = beerRepository.getReferenceById(id);
            beer.setScore(currentBeer.getScore());
            beer.setFinalist(currentBeer.getFinalist());
            beer.setDelivered(currentBeer.getDelivered());
            beer.setPosition(currentBeer.getPosition());
        });

        beerRepository.save(beer);

        return "redirect:/registration/competition/" + competitionId + "/person/" + competitorId + "/beers/";
    }

    @GetMapping(value = {"/{competitionId}/person/{personId}/beers/delete/{beerId}/"})
    public String deleteBeer(@PathVariable("competitionId") UUID competitionId,
                             @PathVariable("personId") UUID competitorId,
                             @PathVariable("beerId") Optional<UUID> oBeerId) {

        final Competition competition = competitionRepository.findById(competitionId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + competitionId));

        if (competition.hasDeadlinePassed()) {
            return "registration-error-deadline-date-passed";
        }

        final Competitor competitor = competitorRepository.findById(competitorId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + competitorId));

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
        return "redirect:/registration/competition/" + competitionId + "/person/" + competitorId + "/beers/";
    }
}
