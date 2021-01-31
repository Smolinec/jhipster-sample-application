package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.SMSNotification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link SMSNotification} entity.
 */
public interface SMSNotificationSearchRepository extends ElasticsearchRepository<SMSNotification, Long> {
}
