package fi.homebrewing.competition.domain;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "competition_category_beer_style")
public class CompetitionCategoryBeerStyle {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Type(type = "uuid-char")
    @Column(columnDefinition = "uniqueidentifier")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "competition_category_id")
    private CompetitionCategory competitionCategory;

    @ManyToOne
    @JoinColumn(name = "beer_style_id")
    private BeerStyle beerStyle;

    //@OneToMany(mappedBy = "competitionCategoryHasBeerStyle")
    //private Set<Beer> beers;

    public CompetitionCategoryBeerStyle() {
    }

    public CompetitionCategoryBeerStyle(CompetitionCategory competitionCategory, BeerStyle beerStyle) {
        this.competitionCategory = competitionCategory;
        this.beerStyle = beerStyle;
    }

    public BeerStyle getBeerStyle() {
        return this.beerStyle;
    }

    public void setBeerStyle(BeerStyle beerStyle) {
        this.beerStyle = beerStyle;
    }

    public CompetitionCategory getCompetitionCategory() {
        return this.competitionCategory;
    }

    public void setCompetitionCategory(CompetitionCategory competitionCategory) {
        this.competitionCategory = competitionCategory;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Transient
    public Set<Beer> getBeers() {
        return Set.of();
    }
}
