package fi.homebrewing.competition.domain;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

@Entity
public class CompetitionCategory {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    @NotBlank(message = "{name.mandatory}")
    private String name;
    private String description;
    @ManyToOne(optional = false)
    private Competition competition;
    @OneToMany(mappedBy="competitionCategory")
    private Set<Beer> beers;

    public CompetitionCategory() {
    }

    public CompetitionCategory(String name, String description, Competition competition) {
        this.name = name;
        this.description = description;
        this.competition = competition;
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

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Beer> getBeers() {
        return beers;
    }

    @Transient
    public String getCompetitionAndName() {
        return competition.getName() + " \\ " + name;
    }
}
