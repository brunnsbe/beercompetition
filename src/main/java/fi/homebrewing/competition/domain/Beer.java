package fi.homebrewing.competition.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Beer {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    @ManyToOne(optional = false)
    private Competitor competitor;
    @NotBlank(message = "{name.mandatory}")
    private String name;
    @NotBlank(message = "Style is mandatory")
    private String style;
    private String comment;

    @Max(value = 30)
    private Double alcoholPercentage;

    @org.hibernate.annotations.ForeignKey(name = "none") // IS THIS OKAY AS WE GET THE FOREIGN KEY CHECK IN WRONG ORDER !?
    @ManyToOne(optional = false)
    private CompetitionCategoryHasBeerStyle competitionCategoryHasBeerStyle;

    public Beer() {
    }

    public Beer(String name, String style, String comment, CompetitionCategoryHasBeerStyle competitionCategoryHasBeerStyle, Competitor competitor, Double alcoholPercentage) {
        this.name = name;
        this.style = style;
        this.comment = comment;
        this.competitionCategoryHasBeerStyle = competitionCategoryHasBeerStyle;
        this.competitor = competitor;
        this.alcoholPercentage = alcoholPercentage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
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

    public CompetitionCategoryHasBeerStyle getCompetitionCategoryHasBeerStyle() {
        return competitionCategoryHasBeerStyle;
    }

    public void setCompetitionCategoryHasBeerStyle(CompetitionCategoryHasBeerStyle competitionCategoryHasBeerStyle) {
        this.competitionCategoryHasBeerStyle = competitionCategoryHasBeerStyle;
    }

    public CompetitionCategory getCompetitionCategory() {
        return this.competitionCategoryHasBeerStyle.getCompetitionCategory();
    }
}
