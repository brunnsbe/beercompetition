package fi.homebrewing.competition.domain;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

@Entity
public class Competition {
    @Nullable
    public String getScoreURL() {
        return scoreURL;
    }

    public void setScoreURL(@Nullable String scoreURL) {
        this.scoreURL = scoreURL;
    }

    public enum Type {
        COMMERCIAL,
        HOMEBREWING,
        MIXED,
        OTHER
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @org.hibernate.annotations.Type(type = "uuid-char")
    @Column(columnDefinition = "uniqueidentifier")
    private UUID id;
    @NotBlank(message = "{name.mandatory}")
    @Size(min = 1, max = 100)
    private String name;
    @Nullable
    @Size(max = 250)
    private String description;

    @Nullable
    @Size(max = 250)
    private String scoreURL;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Nullable
    @Column(columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deadlineDate;

    @Lob
    @Nullable
    private String introductionText;

    @OneToMany(mappedBy = "competition")
    private Set<CompetitionCategory> competitionCategories;

    public Competition() {
    }

    public String getIntroductionText() {
        return introductionText;
    }

    public void setIntroductionText(String introductionText) {
        this.introductionText = introductionText;
    }

    @Nullable
    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(@Nullable Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }


    public boolean hasDeadlinePassed() {
        return deadlineDate != null && new Date().after(deadlineDate);
    }

    public Competition(String name, String description, Type type, Date deadlineDate) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.deadlineDate = deadlineDate;
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

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String comment) {
        this.description = comment;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Set<CompetitionCategory> getCompetitionCategories() {
        return competitionCategories;
    }

    @Transient
    public Set<Beer> getBeers() {
        return competitionCategories.stream()
            .flatMap(v -> v.getCompetitionCategoryBeerStyles().stream())
            .flatMap(v -> v.getBeers().stream())
            .collect(Collectors.toSet());
    }

    @Transient
    public Set<Competitor> getCompetitors() {
        return competitionCategories.stream()
            .flatMap(v -> v.getCompetitionCategoryBeerStyles().stream())
            .flatMap(v -> v.getBeers().stream())
            .map(Beer::getCompetitor)
            .collect(Collectors.toSet());
    }
}
