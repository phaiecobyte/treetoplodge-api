package com.treetoplodge.treetoplodge_api.repository;

import com.treetoplodge.treetoplodge_api.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation,Long> {
    boolean existsAccommodationsByAccommodationId(String accommodationId);

    Optional<Accommodation> findByAccommodationId(String accommodationId);

    void deleteByAccommodationId(String accommodationId);
}
