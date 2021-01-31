package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.DeviceConfiguration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link DeviceConfiguration} entity.
 */
public interface DeviceConfigurationSearchRepository extends ElasticsearchRepository<DeviceConfiguration, Long> {
}
