package fi.homebrewing.competition.domain;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BeerStyleRepository extends JpaRepository<BeerStyle, UUID> {

    List<BeerStyle> findAllByOrderByName();
    @Query("SELECT bs FROM BeerStyle bs WHERE EXISTS (SELECT 1 FROM CompetitionCategoryBeerStyle cchbs WHERE cchbs.beerStyle = bs AND cchbs.competitionCategory IN (:competitionCategories)) ORDER BY bs.name")
    List<BeerStyle> findAllByCompetitionCategoriesInOrderByNameAsc(@Param("competitionCategories") Set<CompetitionCategory> competitionCategories);
}
