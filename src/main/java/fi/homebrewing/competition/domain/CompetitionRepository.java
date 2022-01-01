package fi.homebrewing.competition.domain;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface CompetitionRepository extends CrudRepository<Competition, UUID> {
}
