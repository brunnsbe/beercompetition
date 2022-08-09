package fi.homebrewing.competition.domain;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, String> {

    List<Beer> findAllByCompetitorOrderByNameAsc(Competitor competitor);

}
