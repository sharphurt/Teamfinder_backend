package ru.catstack.teamfinder.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.catstack.teamfinder.service.search.HibernateSearchService;

import javax.persistence.EntityManager;

@Configuration
public class HibernateSearchConfig {

    private final EntityManager entityManager;

    public HibernateSearchConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Bean
    public HibernateSearchService hibernateSearchService(){
        return new HibernateSearchService(entityManager);
    }
}