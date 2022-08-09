package fi.homebrewing.competition.domain;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

@Entity
public class Competition {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @NotBlank(message = "{name.mandatory}")
    private String name;
    @Nullable
    private String description;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Nullable
    @Column(columnDefinition = "DATE")
    private LocalDate deadlineDate;

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

    public enum Type {
        COMMERCIAL,
        HOMEBREWING,
        MIXED,
        OTHER
    }

    public Competition(String name, String description, Type type, LocalDate deadlineDate) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.deadlineDate = deadlineDate;
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
        return competitionCategories.stream().flatMap(v -> v.getCompetitionCategoryHasBeerStyles().stream())
            .flatMap(v -> v.getBeers().stream())
            .collect(Collectors.toSet());
    }

    @Transient
    public Set<Competitor> getCompetitors() {
        return Set.of();
        // ERROR
        /*
        return competitionCategories.stream()
            .flatMap(v -> v.getCompetitionCategoryHasBeerStyles().stream())
            .flatMap(v -> v.getBeers().stream()).map(Beer::getCompetitor)
            .collect(Collectors.toSet());
         */
    }
}
