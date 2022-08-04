package fi.homebrewing.competition.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class BeerStyle {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @NotBlank(message = "{name.mandatory}")
    private String name;
    private String description;

    @ManyToMany(mappedBy = "beerStyles", cascade = CascadeType.MERGE)
    private Set<CompetitionCategory> competitionCategories;

    public BeerStyle() {
    }

    public BeerStyle(String name, String description) {
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CompetitionCategory> getCompetitionCategories() {
        return competitionCategories;
    }

    public void setCompetitionCategories(Set<CompetitionCategory> competitionCategories) {
        this.competitionCategories = competitionCategories;
    }
}
