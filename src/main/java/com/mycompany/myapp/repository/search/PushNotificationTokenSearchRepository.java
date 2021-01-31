package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.PushNotificationToken;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link PushNotificationToken} entity.
 */
public interface PushNotificationTokenSearchRepository extends ElasticsearchRepository<PushNotificationToken, Long> {
}
