package fi.homebrewing.competition.domain;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionRepository extends JpaRepository<Competition, UUID> {
    default List<Competition> getCompetitionsSortedByName() {
        final List<Competition> competitions = findAll();
        competitions.sort(Comparator.comparing(Competition::getName));
        return competitions;
    }
}
