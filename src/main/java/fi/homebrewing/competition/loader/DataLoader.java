package fi.homebrewing.competition.loader;

import java.util.List;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import fi.homebrewing.competition.domain.Beer;
import fi.homebrewing.competition.domain.BeerRepository;
import fi.homebrewing.competition.domain.Competition;
import fi.homebrewing.competition.domain.CompetitionRepository;
import org.springframework.stereotype.Component;

@Component
public record DataLoader(BeerRepository beerRepository, CompetitionRepository competitionRepository) {

    @PostConstruct
    private void loadData() {
        this.competitionRepository.saveAll(
            List.of(
                new Competition("Valtakunnallinen kotiolutkilpailu 2022", "", Competition.Type.HOMEBREWING),
                new Competition("Suomen Paras Olut 2022", "", Competition.Type.COMMERCIAL)
            )
        );

        final List<Competition> test = StreamSupport.stream(this.competitionRepository.findAll().spliterator(), false).toList();

        this.beerRepository.saveAll(
            List.of(
                new Beer("Pale Skin Ale", "American Pale Ale", "", test.get(0), 5.6d),
                new Beer("Witsenhäuser", "Kellerbier", "", test.get(0), 5.1d),
                new Beer("Ruski IS", "Russian Imperial Stout", "", test.get(1), 11d)
            )
        );
    }
}
