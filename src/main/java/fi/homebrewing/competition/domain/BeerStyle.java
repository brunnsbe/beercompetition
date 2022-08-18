package fi.homebrewing.competition.domain;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
public class BeerStyle {
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

    @OneToMany(mappedBy = "beerStyle")
    private Set<CompetitionCategoryBeerStyle> competitionCategoryBeerStyles;

    public BeerStyle() {
    }

    public BeerStyle(String name, String description) {
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CompetitionCategoryBeerStyle> getCompetitionCategoryBeerStyles() {
        return competitionCategoryBeerStyles;
    }

    public void setCompetitionCategoryBeerStyles(Set<CompetitionCategoryBeerStyle> competitionCategoryBeerStyles) {
        this.competitionCategoryBeerStyles = competitionCategoryBeerStyles;
    }
}
