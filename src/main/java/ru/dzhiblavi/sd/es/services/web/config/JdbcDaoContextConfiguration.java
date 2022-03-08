package ru.dzhiblavi.sd.es.services.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.dzhiblavi.sd.es.event.dao.EventDao;
import ru.dzhiblavi.sd.es.event.dao.JdbcEventDao;

import javax.sql.DataSource;

@Configuration
public class JdbcDaoContextConfiguration {
    @Bean
    public EventDao eventDao(DataSource dataSource) {
        return new JdbcEventDao(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:tasks.db");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }
}
