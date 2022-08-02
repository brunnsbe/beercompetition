package fi.homebrewing.competition.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BeerStyleHasCompetitionCategory {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    //@MapsId("beerStyleId")
    //@JoinColumn(name = "beerStyleId")
    BeerStyle beerStyle;

    @ManyToOne
    //@MapsId("competitionCategoryId")
    //@JoinColumn(name = "competition_category_id")
    CompetitionCategory competitionCategory;

    public BeerStyleHasCompetitionCategory() {}

    public BeerStyleHasCompetitionCategory(UUID id, BeerStyle beerStyle, CompetitionCategory competitionCategory) {
        this.id = id;
        this.beerStyle = beerStyle;
        this.competitionCategory = competitionCategory;
    }
}
