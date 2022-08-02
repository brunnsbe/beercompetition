package fi.homebrewing.competition.loader;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import fi.homebrewing.competition.domain.Beer;
import fi.homebrewing.competition.domain.BeerRepository;
import fi.homebrewing.competition.domain.BeerStyle;
import fi.homebrewing.competition.domain.BeerStyleRepository;
import fi.homebrewing.competition.domain.Competition;
import fi.homebrewing.competition.domain.CompetitionCategory;
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
                             BeerStyleRepository beerStyleRepository) {

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
                new BeerStyle(Set.of(), "21 American Ale", "BJCP"),
                new BeerStyle(Set.of(), "5B German Pils", "BJCP")
            )
        );

        final List<CompetitionCategory> competitionCategories = this.competitionCategoryRepository.saveAll(
            List.of(
                new CompetitionCategory("1: Lager ja lager-muistuttavat tyylit", "", competitions.get(0), Set.of(beerStyles.get(0), beerStyles.get(1))),
                new CompetitionCategory("2: Pale ale", "", competitions.get(0), Set.of()),
                new CompetitionCategory("3: Belgialaiset ja vehnäoluet", "", competitions.get(0), Set.of()),
                new CompetitionCategory("4: Stout, Porter ja muut tummat", "", competitions.get(0), Set.of()),
                new CompetitionCategory("5: Hedelmä, hapan- ja villihiivaoluet", "", competitions.get(0), Set.of()),

                new CompetitionCategory("Vaalea lager", "Sarjaan voivat osallistua pohjahiivakäymisellä valmistetut vaaleat lagerit sekä vaaleat bockit.", competitions.get(1), Set.of()),
                new CompetitionCategory("Pils", "Sarjaan voivat osallistua pohjahiivakäymisellä valmistetut perinteiset pils-oluet (pilsner, pilsener).", competitions.get(1), Set.of()),
                new CompetitionCategory("Tumma tai värillinen lager", "Sarjaan voivat osallistua valmistetut pohjahiivakäymisellä keskitummat ja tummat lagerit ja bockit.", competitions.get(1), Set.of()),
                new CompetitionCategory("Vaalea tai keskitumma ale", "Sarjaan voivat osallistua pintahiivakäymisellä valmistetut vaaleat ja keskitummat alet. Sarjan väriskaala voi ulottua tummaan kupariin asti (enintään 50 EBC). Sarjaan voivat osallistua myös sahdit.", competitions.get(1), Set.of()),
                new CompetitionCategory("IPA ja APA", "Sarjaan voivat osallistua erityyppiset India Pale Alet ja American Pale Alet.", competitions.get(1), Set.of()),
                new CompetitionCategory("Stout ja portteri", "Sarjaan voivat osallistua stout- ja portterityyliset oluet vahvuudesta ja käytetystä hiivasta riippumatta.", competitions.get(1), Set.of()),
                new CompetitionCategory("Vehnäolut", "Sarjaan voivat osallistua pinta- tai pohjahiivahiivakäymisellä valmistetut vehnäoluet.", competitions.get(1), Set.of()),
                new CompetitionCategory("Maustamattomat ja maustetut hapanoluet", "Sarjaan voivat osallistua sekä maustamattomat että maustetut, esim. marja-, hedelmä- hapanoluet kuten goset, berliner weisset, sour alet ja muut happamat oluet.", competitions.get(1), Set.of()),
                new CompetitionCategory("Maustetut oluet, kuten hedelmä-, marja- ja muut maustetut oluet", "Sarjaan voivat osallistua eri tavoin maustetut oluet.", competitions.get(1), Set.of()),
                new CompetitionCategory("Kypsytetyt oluet", "Sarjaan voivat osallistua tynnyrissä tai muuten pitkään kypsytetyt oluet.", competitions.get(1), Set.of()),
                new CompetitionCategory("lkoholittomat ja vähäalkoholilliset oluet", "Sarjaan voivat osallistua oluet, joissa on alkoholia enintään 2,8 %.", competitions.get(1), Set.of()),
                new CompetitionCategory("Muut oluet", "Sarjaan voivat osallistua muihin sarjoihin sopimattomat oluet.", competitions.get(1), Set.of())
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
                new Beer("Pale Skin Ale", "American Pale Ale", "", competitionCategories.get(2), competitors.get(0), 5.6d),
                new Beer("Witsenhäuser", "Kellerbier", "", competitionCategories.get(3), competitors.get(0), 5.1d),
                new Beer("Ruski IS", "Russian Imperial Stout", "", competitionCategories.get(11), competitors.get(1), 11d)
            )
        );
    }
}
