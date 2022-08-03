package fi.homebrewing.competition.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionCategoryHasBeerStyleRepository extends JpaRepository<CompetitionCategoryHasBeerStyle, CompetitionCategoryHasBeerStyleKey> {
}
