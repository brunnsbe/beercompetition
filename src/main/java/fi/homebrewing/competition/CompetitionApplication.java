package fi.homebrewing.competition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class CompetitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompetitionApplication.class, args);
    }
}
