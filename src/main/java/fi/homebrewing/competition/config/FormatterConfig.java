package fi.homebrewing.competition.config;

import fi.homebrewing.competition.domain.CompetitionCategoryHasBeerStyleFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FormatterConfig implements WebMvcConfigurer {

    private final CompetitionCategoryHasBeerStyleFormatter competitionCategoryHasBeerStyleFormatter;

    public FormatterConfig(CompetitionCategoryHasBeerStyleFormatter competitionCategoryHasBeerStyleFormatter) {
        this.competitionCategoryHasBeerStyleFormatter = competitionCategoryHasBeerStyleFormatter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(competitionCategoryHasBeerStyleFormatter);
    }
}
