package ru.dzhiblavi.sd.es.services.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.dzhiblavi.sd.es.event.dao.EventDao;
import ru.dzhiblavi.sd.es.event.dao.InMemoryEventDao;

public class InMemoryDaoContextConfiguration {
    @Bean
    public EventDao eventDao() {
        return new InMemoryEventDao();
    }
}
