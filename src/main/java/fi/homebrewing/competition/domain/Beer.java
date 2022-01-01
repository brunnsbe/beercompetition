package fi.homebrewing.competition.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import org.springframework.lang.Nullable;

@Entity(name = "Beer")
@Table(name = "beer")
public class Beer {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private Competition competition;
    @NotBlank(message = "{name.mandatory}")
    private String name;
    @NotBlank(message = "Style is mandatory")
    private String style;
    @Nullable
    private String comment;

    @Nullable
    @Max(value = 30)
    private Double alcoholPercentage;

    public Beer() {}

    public Beer(UUID id, String name, String style, String comment, Competition competition, @Nullable Double alcoholPercentage) {
        this.id = id;
        this.name = name;
        this.style = style;
        this.comment = comment;
        this.competition = competition;
        this.alcoholPercentage = alcoholPercentage;
    }

    public Beer(String name, String style, String comment, Competition competition, @Nullable Double alcoholPercentage) {
        this(UUID.randomUUID(), name, style, comment, competition, alcoholPercentage);
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

    @Nullable
    public Double getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public void setAlcoholPercentage(@Nullable Double alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }
}
