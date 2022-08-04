package fi.homebrewing.competition.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitorRepository extends JpaRepository<Competitor, String> {
}
