package fi.homebrewing.competition.domain;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface BeerRepository extends CrudRepository<Beer, UUID> {
}
