package fi.homebrewing.competition.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompetitionRepository extends JpaRepository<Competition, String> {
    List<Competition> findAllByOrderByName();

    List<Competition> findAllByTypeOrderByName(Competition.Type type);

    @Query("SELECT c FROM Competition c WHERE EXISTS (SELECT 1 FROM CompetitionCategoryHasBeerStyle cchbs " +
        "INNER JOIN CompetitionCategory cc ON cchbs.competitionCategory = cc AND cc.competition = c) ORDER BY c.name")
    List<Competition> findAllByCompetitionCategoryIsNotNullOrderByName();
}
