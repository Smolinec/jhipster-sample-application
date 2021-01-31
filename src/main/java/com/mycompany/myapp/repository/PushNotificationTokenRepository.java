package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PushNotificationToken;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PushNotificationToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PushNotificationTokenRepository extends JpaRepository<PushNotificationToken, Long> {
}
