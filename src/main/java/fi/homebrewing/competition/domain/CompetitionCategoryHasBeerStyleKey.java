package fi.homebrewing.competition.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompetitionCategoryHasBeerStyleKey implements Serializable {
    @Column(name = "competition_category_id")
    private String competitionCategoryId;

    @Column(name = "beer_style_id")
    private String beerStyleId;

    @Override
    public int hashCode() {
        return Objects.hash(competitionCategoryId, beerStyleId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CompetitionCategoryHasBeerStyleKey that)) return false;

        return Objects.equals(this.competitionCategoryId, that.competitionCategoryId)
            && Objects.equals(this.beerStyleId, that.beerStyleId);
    }

    public String getCompetitionCategoryId() {
        return competitionCategoryId;
    }

    public void setCompetitionCategoryId(String competitionCategoryId) {
        this.competitionCategoryId = competitionCategoryId;
    }

    public String getBeerStyleId() {
        return beerStyleId;
    }

    public void setBeerStyleId(String beerStyleId) {
        this.beerStyleId = beerStyleId;
    }
}
