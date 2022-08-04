package fi.homebrewing.competition.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class CompetitionCategory {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @NotBlank(message = "{name.mandatory}")
    private String name;
    private String description;
    @ManyToOne(optional = false)
    private Competition competition;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "competition_category_has_beer_style",
        joinColumns = @JoinColumn(name = "competition_category_id"),
        inverseJoinColumns = @JoinColumn(name = "beer_style_id"))
    private Set<BeerStyle> beerStyles;

    @OneToMany(mappedBy = "competitionCategory")
    private Set<CompetitionCategoryHasBeerStyle> competitionCategoryHasBeerStyles;

    public CompetitionCategory() {
    }

    public CompetitionCategory(String name, String description, Competition competition, Set<BeerStyle> beerStyles) {
        this.name = name;
        this.description = description;
        this.competition = competition;
        this.beerStyles = beerStyles;
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

    public Set<BeerStyle> getBeerStyles() {
        return beerStyles;
    }

    public void setBeerStyles(Set<BeerStyle> beerStyles) {
        this.beerStyles = beerStyles;
    }

    public Set<CompetitionCategoryHasBeerStyle> getCompetitionCategoryHasBeerStyles() {
        return competitionCategoryHasBeerStyles;
    }
}
