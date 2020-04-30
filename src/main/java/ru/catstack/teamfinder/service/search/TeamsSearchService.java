package ru.catstack.teamfinder.service.search;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.teamfinder.model.Team;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TeamsSearchService {

    private HibernateSearchService hibernateSearchService;

    @Autowired
    public TeamsSearchService(HibernateSearchService hibernateSearchService) {
        this.hibernateSearchService = hibernateSearchService;
    }

    @PostConstruct
    public void initHibernateSearch() throws Exception{
        hibernateSearchService.initializeHibernateSearch();
    }
    @Transactional
    public List<Team> findTeamsByKeyword(String keyword, int from, int count){
        var fullTextEntityManager = Search.getFullTextEntityManager(hibernateSearchService.getEntityManager());
        fullTextEntityManager.flushToIndexes();
        var queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Team.class)
                .get();
        var luceneQuery = queryBuilder
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(1)
                .withPrefixLength(1)
                .onFields("name", "tagsList")
                .matching(keyword)
                .createQuery();
        var jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Team.class)
                .setFirstResult(from).setMaxResults(from + count);
        return jpaQuery.getResultList();
    }
}