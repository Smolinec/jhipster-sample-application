package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DeviceProfile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the DeviceProfile entity.
 */
@Repository
public interface DeviceProfileRepository extends JpaRepository<DeviceProfile, Long> {

    @Query(value = "select distinct deviceProfile from DeviceProfile deviceProfile left join fetch deviceProfile.devices",
        countQuery = "select count(distinct deviceProfile) from DeviceProfile deviceProfile")
    Page<DeviceProfile> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct deviceProfile from DeviceProfile deviceProfile left join fetch deviceProfile.devices")
    List<DeviceProfile> findAllWithEagerRelationships();

    @Query("select deviceProfile from DeviceProfile deviceProfile left join fetch deviceProfile.devices where deviceProfile.id =:id")
    Optional<DeviceProfile> findOneWithEagerRelationships(@Param("id") Long id);
}
