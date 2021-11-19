package dk.kvalitetsit.regsj.testkomponent.configuration;

import dk.kvalitetsit.regsj.testkomponent.dao.LastAccessedDao;
import dk.kvalitetsit.regsj.testkomponent.dao.LastAccessedDaoImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {
    @Bean
    public LastAccessedDao helloDao(DataSource dataSource) {
        return new LastAccessedDaoImpl(dataSource);
    }

    @Bean
    public DataSource dataSource(@Value("${jdbc.url}") String jdbcUrl, @Value("${jdbc.user}") String jdbcUser, @Value("${jdbc.pass}") String jdbcPass) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUser);
        dataSource.setPassword(jdbcPass);

        return dataSource;
    }
}