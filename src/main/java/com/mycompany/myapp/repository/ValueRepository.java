package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Value entity.
 */
@Repository
public interface ValueRepository extends JpaRepository<Value, Long> {

    @Query(value = "select distinct value from Value value left join fetch value.temperatures",
        countQuery = "select count(distinct value) from Value value")
    Page<Value> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct value from Value value left join fetch value.temperatures")
    List<Value> findAllWithEagerRelationships();

    @Query("select value from Value value left join fetch value.temperatures where value.id =:id")
    Optional<Value> findOneWithEagerRelationships(@Param("id") Long id);
}
