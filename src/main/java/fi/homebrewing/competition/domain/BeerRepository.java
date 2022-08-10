package fi.homebrewing.competition.domain;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

    List<Beer> findAllByCompetitorOrderByNameAsc(Competitor competitor);

}
