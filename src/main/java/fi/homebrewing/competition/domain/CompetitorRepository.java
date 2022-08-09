package fi.homebrewing.competition.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

public interface CompetitorRepository extends JpaRepository<Competitor, String> {
    Optional<Competitor> findByEmailAddress(String emailAddress);

    List<Competition> findAllByOrderByLastNameAscFirstNameAsc();
}
