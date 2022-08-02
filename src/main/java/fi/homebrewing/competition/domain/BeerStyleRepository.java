package fi.homebrewing.competition.domain;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerStyleRepository extends JpaRepository<BeerStyle, UUID> {
}
