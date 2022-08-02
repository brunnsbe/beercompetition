package fi.homebrewing.competition.domain;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class BeerStyle {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    @ManyToMany
    /*
    @JoinTable(
        name = "BeerStyleHasCompetitionCategory",
        joinColumns = @JoinColumn(name = "beerStyleId"),
        inverseJoinColumns = @JoinColumn(name = "competitionCategoryId")
    )
     */
    private Set<CompetitionCategory> competitionCategories;
    @NotBlank(message = "{name.mandatory}")
    private String name;
    private String description;

    public BeerStyle() {
    }

    public BeerStyle(Set<CompetitionCategory> competitionCategories, String name, String description) {
        this.competitionCategories = competitionCategories;
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<CompetitionCategory> getCompetitionCategories() {
        return competitionCategories;
    }

    public void setCompetitionCategories(Set<CompetitionCategory> competitionCategory) {
        this.competitionCategories = competitionCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
