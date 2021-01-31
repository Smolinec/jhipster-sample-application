package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Place;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Place} entity.
 */
public interface PlaceSearchRepository extends ElasticsearchRepository<Place, Long> {
}
