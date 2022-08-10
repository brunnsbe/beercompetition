package fi.homebrewing.competition.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionCategoryRepository extends JpaRepository<CompetitionCategory, UUID> {
    default List<CompetitionCategory> findAll(Competition competition) {
        final CompetitionCategory competitionCategory = new CompetitionCategory();
        competitionCategory.setCompetition(competition);
        Example<CompetitionCategory> example = Example.of(competitionCategory);
        return findAll(example, Sort.by(Sort.Direction.ASC, "name"));
    }

    List<CompetitionCategory> findDistinctByBeerStylesIsNotNullOrderByName();
}

