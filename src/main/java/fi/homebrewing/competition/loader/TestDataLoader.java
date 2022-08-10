package fi.homebrewing.competition.loader;

import java.util.List;

import javax.annotation.PostConstruct;

import fi.homebrewing.competition.domain.Beer;
import fi.homebrewing.competition.domain.BeerRepository;
import fi.homebrewing.competition.domain.BeerStyle;
import fi.homebrewing.competition.domain.BeerStyleRepository;
import fi.homebrewing.competition.domain.Competition;
import fi.homebrewing.competition.domain.CompetitionCategory;
import fi.homebrewing.competition.domain.CompetitionCategoryBeerStyle;
import fi.homebrewing.competition.domain.CompetitionCategoryBeerStyleRepository;
import fi.homebrewing.competition.domain.CompetitionCategoryRepository;
import fi.homebrewing.competition.domain.CompetitionRepository;
import fi.homebrewing.competition.domain.Competitor;
import fi.homebrewing.competition.domain.CompetitorRepository;
import org.springframework.stereotype.Component;

@Component
public record TestDataLoader(BeerRepository beerRepository,
                             CompetitionRepository competitionRepository,
                             CompetitionCategoryRepository competitionCategoryRepository,
                             CompetitorRepository competitorRepository,
                             BeerStyleRepository beerStyleRepository,
                             CompetitionCategoryBeerStyleRepository competitionCategoryBeerStyleRepository) {

    @PostConstruct
    private void loadData() {
        final List<Competition> competitions = this.competitionRepository.saveAll(
            List.of(
                new Competition("Valtakunnallinen kotiolutkilpailu 2022", "Finland's oldest homebrewing competition", Competition.Type.HOMEBREWING, null),
                new Competition("Suomen Paras Olut 2022", "Best commercial beer of Finland", Competition.Type.COMMERCIAL, null)
            )
        );

        final List<BeerStyle> beerStyles = this.beerStyleRepository.saveAll(
            List.of(
                new BeerStyle("1A American Light Lager", "BJCP"),
                new BeerStyle("21A American IPA", "BJCP"),
                new BeerStyle("5B German Pils", "BJCP"),
                new BeerStyle("25B Saison", "BJCP")
            )
        );

        final List<CompetitionCategory> competitionCategories = this.competitionCategoryRepository.saveAll(
            List.of(
                new CompetitionCategory("1: Lager ja lager-muistuttavat tyylit", "", competitions.get(0)),
                new CompetitionCategory("2: Pale ale", "", competitions.get(0)),
                new CompetitionCategory("3: Belgialaiset ja vehnäoluet", "", competitions.get(0)),
                new CompetitionCategory("4: Stout, Porter ja muut tummat", "", competitions.get(0)),
                new CompetitionCategory("5: Hedelmä, hapan- ja villihiivaoluet", "", competitions.get(0))
            )
        );

        final List<CompetitionCategoryBeerStyle> competitionCategoryBeerStyles = this.competitionCategoryBeerStyleRepository.saveAll(
            List.of(
                new CompetitionCategoryBeerStyle(competitionCategories.get(0), beerStyles.get(0)),
                new CompetitionCategoryBeerStyle(competitionCategories.get(0), beerStyles.get(2)),
                new CompetitionCategoryBeerStyle(competitionCategories.get(1), beerStyles.get(1)),
                new CompetitionCategoryBeerStyle(competitionCategories.get(2), beerStyles.get(3))
            )
        );

        final List<Competitor> competitors = this.competitorRepository.saveAll(
            List.of(
                new Competitor("André", "Brunnsberg", "Esbo", "+358405353900", "andre.brunnsberg@gmail.com"),
                new Competitor("Patrik", "Pesonius", "Helsinki", "+123", "patrik.pesonius@gmail.com")
            )
        );

        this.beerRepository.saveAll(
            List.of(
                new Beer("Pale Skin Ale", "", competitionCategoryBeerStyles.get(0), competitors.get(0), 5.6d),
                new Beer("Witsenhäuser", "", competitionCategoryBeerStyles.get(0), competitors.get(0), 5.1d),
                new Beer("Ruski IS", "", competitionCategoryBeerStyles.get(3), competitors.get(1), 11d)
            )
        );
    }
}
