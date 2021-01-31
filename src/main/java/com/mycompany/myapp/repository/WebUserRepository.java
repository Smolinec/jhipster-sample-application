package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WebUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the WebUser entity.
 */
@Repository
public interface WebUserRepository extends JpaRepository<WebUser, Long> {

    @Query(value = "select distinct webUser from WebUser webUser left join fetch webUser.pushNotificationTokens",
        countQuery = "select count(distinct webUser) from WebUser webUser")
    Page<WebUser> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct webUser from WebUser webUser left join fetch webUser.pushNotificationTokens")
    List<WebUser> findAllWithEagerRelationships();

    @Query("select webUser from WebUser webUser left join fetch webUser.pushNotificationTokens where webUser.id =:id")
    Optional<WebUser> findOneWithEagerRelationships(@Param("id") Long id);
}
