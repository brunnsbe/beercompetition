package fi.homebrewing.competition.htmlcontroller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import fi.homebrewing.competition.domain.BeerStyle;
import fi.homebrewing.competition.domain.BeerStyleRepository;
import fi.homebrewing.competition.domain.CompetitionCategory;
import fi.homebrewing.competition.domain.CompetitionCategoryRepository;
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
@RequestMapping("/admin/beer-styles")
public class HtmlAdminBeerStyleController {
    private static final String THYMELEAF_TEMPLATE_BEER_STYLE_LIST = "beer-style-list";
    private static final String THYMELEAF_TEMPLATE_BEER_STYLE_FORM = "beer-style-form";

    private static final String ACTIVE_PAGE = "/admin/beer-styles/";

    private final BeerStyleRepository beerStyleRepository;
    private final CompetitionCategoryRepository competitionCategoryRepository;

    public HtmlAdminBeerStyleController(BeerStyleRepository beerStyleRepository,
                                        CompetitionCategoryRepository competitionCategoryRepository) {

        this.beerStyleRepository = beerStyleRepository;
        this.competitionCategoryRepository = competitionCategoryRepository;
    }

    @GetMapping("/")
    public String getBeerStylesList(Model model,
                                    @Nullable @Param("competitionCategory") CompetitionCategory competitionCategory) {

        model.addAttribute("activePage", ACTIVE_PAGE);

        final List<BeerStyle> beerStyles = beerStyleRepository.findAll(); // TODO: Add filtering by competition category

        // Filters
        model.addAttribute(
            "competitionCategories",
            beerStyles.stream().flatMap(v -> v.getCompetitionCategories().stream()).distinct().sorted().toList()
        );
        model.addAttribute("competitionCategory", competitionCategory);

        // Table
        model.addAttribute("beerStyles", beerStyles);

        return THYMELEAF_TEMPLATE_BEER_STYLE_LIST;
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String getBeerStyleForm(@PathVariable("id") Optional<UUID> oId, Model model) {
        model.addAttribute("activePage", ACTIVE_PAGE);

        final BeerStyle beerStyle = oId.map(id -> {
            return beerStyleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        }).orElseGet(BeerStyle::new);

        model.addAttribute("beerStyle", beerStyle);

        return THYMELEAF_TEMPLATE_BEER_STYLE_FORM;
    }

    @PostMapping(value = {"/upsert", "/upsert/{id}"})
    public String upsertCompetition(@PathVariable("id") Optional<UUID> oId, @Valid BeerStyle beerStyle, BindingResult result) {
        oId.ifPresent(beerStyle::setId);

        if (result.hasErrors()) {
            return THYMELEAF_TEMPLATE_BEER_STYLE_FORM;
        }

        beerStyleRepository.save(beerStyle);
        return "redirect:" + ACTIVE_PAGE;
    }

    @GetMapping("/delete/{id}")
    public String deleteCompetition(@PathVariable("id") UUID id) {
        BeerStyle beerStyle = beerStyleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));

        beerStyleRepository.delete(beerStyle);
        return "redirect:" + ACTIVE_PAGE;
    }
}
