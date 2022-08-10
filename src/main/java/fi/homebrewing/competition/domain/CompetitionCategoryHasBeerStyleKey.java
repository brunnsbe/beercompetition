package fi.homebrewing.competition.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Embeddable
public class CompetitionCategoryHasBeerStyleKey implements Serializable {
    @Column(name = "competition_category_id", columnDefinition = "uniqueidentifier")
    @Type(type = "uuid-char")
    private UUID competitionCategoryId;

    @Column(name = "beer_style_id", columnDefinition = "uniqueidentifier")
    @Type(type = "uuid-char")
    private UUID beerStyleId;

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

    @Override
    public String toString() {
        return competitionCategoryId + "|" + beerStyleId;
    }

    public UUID getCompetitionCategoryId() {
        return competitionCategoryId;
    }

    public void setCompetitionCategoryId(UUID competitionCategoryId) {
        this.competitionCategoryId = competitionCategoryId;
    }

    public UUID getBeerStyleId() {
        return beerStyleId;
    }

    public void setBeerStyleId(UUID beerStyleId) {
        this.beerStyleId = beerStyleId;
    }


}
