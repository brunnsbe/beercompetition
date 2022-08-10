package fi.homebrewing.competition.domain;

import java.text.ParseException;
import java.util.Locale;
import java.util.UUID;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;

@Service
public class CompetitionCategoryHasBeerStyleFormatter implements Formatter<CompetitionCategoryHasBeerStyle> {
    private final CompetitionCategoryHasBeerStyleRepository competitionCategoryHasBeerStyleRepository;

    public CompetitionCategoryHasBeerStyleFormatter(CompetitionCategoryHasBeerStyleRepository competitionCategoryHasBeerStyleRepository) {
        this.competitionCategoryHasBeerStyleRepository = competitionCategoryHasBeerStyleRepository;
    }

    @Override
    public CompetitionCategoryHasBeerStyle parse(String text, Locale locale) throws ParseException {
        final int pipeIndex = text.indexOf('|');
        if (pipeIndex == -1) return new CompetitionCategoryHasBeerStyle();

        final CompetitionCategoryHasBeerStyleKey competitionCategoryHasBeerStyleKey = new CompetitionCategoryHasBeerStyleKey();
        competitionCategoryHasBeerStyleKey.setCompetitionCategoryId(UUID.fromString(text.substring(0, pipeIndex)));
        competitionCategoryHasBeerStyleKey.setBeerStyleId(UUID.fromString(text.substring(pipeIndex + 1)));

        return competitionCategoryHasBeerStyleRepository.findById(competitionCategoryHasBeerStyleKey)
            .orElse(new CompetitionCategoryHasBeerStyle());
    }

    @Override
    public String print(CompetitionCategoryHasBeerStyle object, Locale locale) {
        return object.getId().toString();
    }
}
