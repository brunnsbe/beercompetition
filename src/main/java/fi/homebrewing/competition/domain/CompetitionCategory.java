package fi.homebrewing.competition.domain;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
public class CompetitionCategory {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Type(type = "uuid-char")
    @Column(columnDefinition = "uniqueidentifier")
    private UUID id;
    @NotBlank(message = "{name.mandatory}")
    @Size(min = 1, max = 100)
    private String name;
    @Size(max = 250)
    private String description;
    @ManyToOne(optional = false)
    private Competition competition;

    @OneToMany(mappedBy = "competitionCategory")
    private Set<CompetitionCategoryBeerStyle> competitionCategoryBeerStyles;

    @Transient
    private Set<BeerStyle> beerStyles;

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

    @Transient
    public String getCompetitionAndName() {
        return competition.getName() + " \\ " + name;
    }

    public Set<CompetitionCategoryBeerStyle> getCompetitionCategoryBeerStyles() {
        return competitionCategoryBeerStyles;
    }

    public Set<BeerStyle> getBeerStyles() {
        if (competitionCategoryBeerStyles == null) {
            return beerStyles;
        } else {
            return competitionCategoryBeerStyles.stream().map(CompetitionCategoryBeerStyle::getBeerStyle).collect(Collectors.toSet());
        }
    }

    public void setBeerStyles(Set<BeerStyle> beerStyles) {
        this.beerStyles = beerStyles;
    }

    public void setCompetitionCategoryBeerStyles(Set<CompetitionCategoryBeerStyle> competitionCategoryBeerStyles) {
        this.competitionCategoryBeerStyles = competitionCategoryBeerStyles;
    }
}
