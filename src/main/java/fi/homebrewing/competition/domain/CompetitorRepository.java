package fi.homebrewing.competition.domain;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitorRepository extends JpaRepository<Competitor, UUID> {
}
