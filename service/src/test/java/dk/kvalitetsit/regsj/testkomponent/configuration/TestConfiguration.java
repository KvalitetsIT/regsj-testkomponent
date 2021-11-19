package dk.kvalitetsit.regsj.testkomponent.configuration;

import dk.kvalitetsit.regsj.testkomponent.dao.LastAccessedDao;
import dk.kvalitetsit.regsj.testkomponent.dao.LastAccessedDaoImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class TestConfiguration {
    // Configure beans used for test

    @Bean
    public LastAccessedDao helloDao(DataSource dataSource) {
        return new LastAccessedDaoImpl(dataSource);
    }
}
