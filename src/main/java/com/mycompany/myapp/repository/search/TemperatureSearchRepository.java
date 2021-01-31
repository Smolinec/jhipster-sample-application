package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Temperature;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Temperature} entity.
 */
public interface TemperatureSearchRepository extends ElasticsearchRepository<Temperature, Long> {
}
