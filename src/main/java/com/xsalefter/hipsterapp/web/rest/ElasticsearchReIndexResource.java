package com.xsalefter.hipsterapp.web.rest;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.xsalefter.hipsterapp.security.AuthoritiesConstants;
import com.xsalefter.hipsterapp.security.SecurityUtils;
import com.xsalefter.hipsterapp.service.ElasticSearchReindexService;
import com.xsalefter.hipsterapp.web.rest.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class ElasticsearchReIndexResource {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchReIndexResource.class);

    private final ElasticSearchReindexService elasticSearchReindexService;

    public ElasticsearchReIndexResource(final ElasticSearchReindexService service) {
        this.elasticSearchReindexService = service;
    }

    /**
     * POST  /elasticsearch/index -> Reindex all Elasticsearch documents
     */
    @RequestMapping(value = "/elasticsearch/reindex",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    @PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
    public ResponseEntity<Void> reindexAll() throws URISyntaxException {
        log.info("REST request to reindex Elasticsearch by user : {}", SecurityUtils.getCurrentUserLogin());
        this.elasticSearchReindexService.reIndex();
        return ResponseEntity.accepted()
            .headers(HeaderUtil.createAlert("elasticsearch.reindex.accepted", null))
            .build();
    }
}
