package fi.homebrewing.competition.domain;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompetitionCategoryBeerStyleRepository extends JpaRepository<CompetitionCategoryBeerStyle, UUID> {
    List<CompetitionCategoryBeerStyle> findAllByCompetitionCategory(CompetitionCategory competitionCategory);

    @Query("SELECT ccbs FROM CompetitionCategoryBeerStyle ccbs" +
        " INNER JOIN CompetitionCategory cc ON ccbs.competitionCategory = cc" +
        " INNER JOIN BeerStyle bs ON ccbs.beerStyle = bs" +
        " WHERE cc.competition = (:competition) ORDER BY cc.name, bs.name")
    List<CompetitionCategoryBeerStyle> findAllByCompetitionOrderByCategoryNameAscBeerStyleAsc(@Param("competition") Competition competition);
}
