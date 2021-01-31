package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Values;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Values entity.
 */
@Repository
public interface ValuesRepository extends JpaRepository<Values, Long> {

    @Query(value = "select distinct values from Values values left join fetch values.temperatures",
        countQuery = "select count(distinct values) from Values values")
    Page<Values> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct values from Values values left join fetch values.temperatures")
    List<Values> findAllWithEagerRelationships();

    @Query("select values from Values values left join fetch values.temperatures where values.id =:id")
    Optional<Values> findOneWithEagerRelationships(@Param("id") Long id);
}
