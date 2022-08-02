package fi.homebrewing.competition.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Entity
public class Beer {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    @ManyToOne(optional = false)
    private CompetitionCategory competitionCategory;
    @ManyToOne(optional = false)
    private Competitor competitor;
    @NotBlank(message = "{name.mandatory}")
    private String name;
    @NotBlank(message = "Style is mandatory")
    private String style;
    private String comment;

    @Max(value = 30)
    private Double alcoholPercentage;

    public Beer() {
    }

    public Beer(String name, String style, String comment, CompetitionCategory competitionCategory, Competitor competitor, Double alcoholPercentage) {
        this.name = name;
        this.style = style;
        this.comment = comment;
        this.competitionCategory = competitionCategory;
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

    public CompetitionCategory getCompetitionCategory() {
        return competitionCategory;
    }

    public void setCompetitionCategory(CompetitionCategory competitionCategory) {
        this.competitionCategory = competitionCategory;
    }
}
