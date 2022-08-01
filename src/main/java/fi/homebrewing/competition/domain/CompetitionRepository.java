package fi.homebrewing.competition.domain;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

public interface CompetitionRepository extends JpaRepository<Competition, UUID> {
    default List<Competition> getCompetitionsSortedByName(@Nullable Competition.Type type) {
        final Competition competition = new Competition();
        competition.setType(type);
        Example<Competition> competitionExample = Example.of(competition);

        return findAll(competitionExample, Sort.by(Sort.Direction.ASC, "name"));
    }
}
