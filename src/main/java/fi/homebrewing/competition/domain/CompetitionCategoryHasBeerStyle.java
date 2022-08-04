package fi.homebrewing.competition.domain;

import java.util.Set;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

@Entity
@Table(name = "competition_category_has_beer_style")
public class CompetitionCategoryHasBeerStyle {
    @EmbeddedId
    private CompetitionCategoryHasBeerStyleKey id;

    @ManyToOne
    @MapsId("competitionCategoryId")
    @JoinColumn(name = "competition_category_id")
    private CompetitionCategory competitionCategory;

    @ManyToOne
    @MapsId("beerStyleId")
    @JoinColumn(name = "beer_style_id")
    private BeerStyle beerStyle;

    @OneToMany(mappedBy = "competitionCategoryHasBeerStyle")
    private Set<Beer> beers;

    public CompetitionCategoryHasBeerStyle() {
    }

    public CompetitionCategoryHasBeerStyle(CompetitionCategory competitionCategory, BeerStyle beerStyle) {
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

    public CompetitionCategoryHasBeerStyleKey getId() {
        return id;
    }

    public void setId(CompetitionCategoryHasBeerStyleKey id) {
        this.id = id;
    }

    public Set<Beer> getBeers() {
        return beers;
    }
}
