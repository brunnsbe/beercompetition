package fi.homebrewing.competition.domain;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
public class Beer {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Type(type = "uuid-char")
    @Column(columnDefinition = "uniqueidentifier")
    private UUID id;
    @ManyToOne(optional = false)
    private Competitor competitor;
    @ManyToOne(optional = false)
    @JoinColumn(name = "competition_category_has_beer_style_id",referencedColumnName = "id")
    private CompetitionCategoryBeerStyle competitionCategoryBeerStyle;
    @NotBlank(message = "{name.mandatory}")
    @Size(min = 1, max = 100)
    private String name;
    @Size(max = 250)
    private String comment;

    @Max(value = 30)
    private Double alcoholPercentage;

    @Max(value = 1000)
    private Double ibu;

    @Max(value = 1000)
    private Double ebc;

    private Double score;

    private Boolean finalist;

    private Integer position;

    private Boolean delivered;

    // Notice, that we here use SQL Server column definition for the sequence
    @Generated(GenerationTime.INSERT)
    @Column(columnDefinition = "int identity(1,1)", updatable = false)
    private long sequenceNumber;

    public Beer() {
    }

    public Beer(String name, String comment, CompetitionCategoryBeerStyle competitionCategoryBeerStyle, Competitor competitor, Double alcoholPercentage) {
        this.name = name;
        this.comment = comment;
        this.competitionCategoryBeerStyle = competitionCategoryBeerStyle;
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

    public CompetitionCategoryBeerStyle getCompetitionCategoryBeerStyle() {
        return competitionCategoryBeerStyle;
    }

    public void setCompetitionCategoryBeerStyle(CompetitionCategoryBeerStyle competitionCategoryBeerStyle) {
        this.competitionCategoryBeerStyle = competitionCategoryBeerStyle;
    }

    public Double getIbu() {
        return ibu;
    }

    public void setIbu(Double ibu) {
        this.ibu = ibu;
    }

    public Double getEbc() {
        return ebc;
    }

    public void setEbc(Double ebc) {
        this.ebc = ebc;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getBeerNumber() {
        Pattern numberPattern = Pattern.compile("\\d+");

        final String beerStyle = getCompetitionCategoryBeerStyle().getBeerStyle().getName();
        final String competitionCategory = getCompetitionCategoryBeerStyle().getCompetitionCategory().getName();
        try {
            final Matcher matcher = numberPattern.matcher(competitionCategory);
            if (matcher.find()) {
                final long competitionCategoryAsLong = Long.parseLong(matcher.group(0));
                return competitionCategoryAsLong + "-" + beerStyle.substring(0, beerStyle.indexOf(' ')) + "-" + sequenceNumber;
            }
        } catch (NumberFormatException e) {
            // Just skip
        }
        return beerStyle.substring(0, beerStyle.indexOf(' ')) + "-" + sequenceNumber;
    }

    public Boolean getFinalist() {
        return finalist;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public void setFinalist(Boolean finalist) {
        this.finalist = finalist;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }
}
