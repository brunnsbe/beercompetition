package fi.homebrewing.competition.htmlcontroller;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.Beer;
import fi.homebrewing.competition.domain.BeerRepository;
import fi.homebrewing.competition.domain.CompetitionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/beers")
public class HtmlBeerController {
    private static final String THYMELEAF_TEMPLATE_BEER_LIST = "beer-list";
    private static final String THYMELEAF_TEMPLATE_BEER_FORM = "beer-form";

    private final BeerRepository beerRepository;
    private final CompetitionRepository competitionRepository;

    public HtmlBeerController(BeerRepository beerRepository, CompetitionRepository competitionRepository) {
        this.beerRepository = beerRepository;
        this.competitionRepository = competitionRepository;
    }

    @GetMapping("/")
    public String getBeersList(Model model) {
        model.addAttribute("currentBeers", beerRepository.findAll());
        return THYMELEAF_TEMPLATE_BEER_LIST;
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String getBeerForm(@PathVariable("id") Optional<UUID> oId, Model model) {

        final Beer beer = oId.map(id -> {
            return beerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid beer Id:" + id));
        }).orElseGet(Beer::new);
        model.addAttribute("beer", beer);

        model.addAttribute("competitions", competitionRepository.findAll());

        return THYMELEAF_TEMPLATE_BEER_FORM;
    }

    @PostMapping(value = {"/upsert", "/upsert/{id}"})
    public String upsertBeer(@PathVariable("id") Optional<UUID> oId, @Valid Beer beer, BindingResult result, Model model) {
        oId.ifPresent(beer::setId);

        if (result.hasErrors()) {
            model.addAttribute("competitions", competitionRepository.findAll());
            return THYMELEAF_TEMPLATE_BEER_FORM;
        }

        beerRepository.save(beer);
        return "redirect:/admin/beers/";
    }

    @GetMapping("/delete/{id}")
    public String deleteBeer(@PathVariable("id") UUID id) {
        Beer user = beerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid beer Id:" + id));

        beerRepository.delete(user);
        return "redirect:/admin/beers/";
    }
}
