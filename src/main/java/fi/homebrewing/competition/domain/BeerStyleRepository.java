package fi.homebrewing.competition.domain;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerStyleRepository extends JpaRepository<BeerStyle, UUID> {

    List<BeerStyle> findAllByOrderByName();
    List<BeerStyle> findAllByCompetitionCategoriesInOrderByNameAsc(Set<CompetitionCategory> competitionCategory);
}
