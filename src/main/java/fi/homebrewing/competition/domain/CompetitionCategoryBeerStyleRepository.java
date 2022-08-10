package fi.homebrewing.competition.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionCategoryBeerStyleRepository extends JpaRepository<CompetitionCategoryBeerStyle, UUID> {
    List<CompetitionCategoryBeerStyle> findAllByCompetitionCategory(CompetitionCategory competitionCategory);
}
