package fi.homebrewing.competition.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
public class Beer {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Type(type = "uuid-char")
    @Column(columnDefinition = "uniqueidentifier")
    private UUID id;
    @ManyToOne(optional = false)
    private Competitor competitor;
    @ManyToOne(optional = false)
    @JoinColumn(name = "competition_category_has_beer_style_id",referencedColumnName = "id")
    private CompetitionCategoryBeerStyle competitionCategoryBeerStyle;
    @NotBlank(message = "{name.mandatory}")
    private String name;
    private String comment;

    @Max(value = 30)
    private Double alcoholPercentage;

    private Double ibu;

    private Double ebc;

    private Double score;

    public Beer() {
    }

    public Beer(String name, String comment, CompetitionCategoryBeerStyle competitionCategoryBeerStyle, Competitor competitor, Double alcoholPercentage) {
        this.name = name;
        this.comment = comment;
        this.competitionCategoryBeerStyle = competitionCategoryBeerStyle;
        this.competitor = competitor;
        this.alcoholPercentage = alcoholPercentage;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public void setAlcoholPercentage(Double alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }

    public CompetitionCategoryBeerStyle getCompetitionCategoryBeerStyle() {
        return competitionCategoryBeerStyle;
    }

    public void setCompetitionCategoryBeerStyle(CompetitionCategoryBeerStyle competitionCategoryBeerStyle) {
        this.competitionCategoryBeerStyle = competitionCategoryBeerStyle;
    }

    public Double getIbu() {
        return ibu;
    }

    public void setIbu(Double ibu) {
        this.ibu = ibu;
    }

    public Double getEbc() {
        return ebc;
    }

    public void setEbc(Double ebc) {
        this.ebc = ebc;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
