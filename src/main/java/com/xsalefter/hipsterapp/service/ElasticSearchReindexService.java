package com.xsalefter.hipsterapp.service;

import java.io.Serializable;

import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codahale.metrics.annotation.Timed;
import com.xsalefter.hipsterapp.domain.City;
import com.xsalefter.hipsterapp.repository.CityRepository;
import com.xsalefter.hipsterapp.repository.search.CitySearchRepository;

@Service
public class ElasticSearchReindexService {

    private static final Logger log = LoggerFactory.getLogger(ElasticSearchReindexService.class);

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CityRepository cityRepository;
    private final CitySearchRepository citySearchRepository;

    public ElasticSearchReindexService(
            ElasticsearchTemplate elasticsearchTemplate,
            CityRepository cityRepository, 
            CitySearchRepository citySearchRepository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.cityRepository = cityRepository;
        this.citySearchRepository = citySearchRepository;
    }

    @Async
    @Timed
    @Transactional(readOnly = true)
    public void reIndex() {
        log.debug("Trying to reindex elasticsearch data. This action will destroy all previous data.");
        reIndexEntity(City.class, this.elasticsearchTemplate, this.cityRepository, this.citySearchRepository);
    }

    private static final <E, ID extends Serializable> void reIndexEntity(
            Class<E> entityClass,
            ElasticsearchTemplate elasticsearchTemplate,
            JpaRepository<E, ID> jpa,
            ElasticsearchRepository<E, ID> elastic) {
        final String entityName = entityClass.getSimpleName();
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
        } finally {
            elasticsearchTemplate.putMapping(entityClass);
        }

        if (jpa.count() > 0) {
            elastic.save(jpa.findAll());
        } else {
            log.info("Try to re-index {} data, but there's 0 {} data in RDBMS database.", entityName, entityName);
        }
        log.info("Reindexing process for entity {} already completed.", entityName);
    }
}
