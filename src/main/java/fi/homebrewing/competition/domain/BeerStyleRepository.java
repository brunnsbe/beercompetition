package fi.homebrewing.competition.domain;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerStyleRepository extends JpaRepository<BeerStyle, String> {

    List<BeerStyle> findAllByOrderByName();
    List<BeerStyle> findAllByCompetitionCategoriesInOrderByNameAsc(Set<CompetitionCategory> competitionCategory);
}
