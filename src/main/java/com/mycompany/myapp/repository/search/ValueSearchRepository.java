package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Value;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Value} entity.
 */
public interface ValueSearchRepository extends ElasticsearchRepository<Value, Long> {
}
