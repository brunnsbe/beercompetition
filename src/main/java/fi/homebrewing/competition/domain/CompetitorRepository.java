package fi.homebrewing.competition.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitorRepository extends JpaRepository<Competitor, UUID> {
    Optional<Competitor> findByEmailAddress(String emailAddress);

    List<Competitor> findAllByOrderByLastNameAscFirstNameAsc();
}
