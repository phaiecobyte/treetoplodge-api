package com.treetoplodge.treetoplodge_api.service;

import com.treetoplodge.treetoplodge_api.exception.AppException;
import com.treetoplodge.treetoplodge_api.model.Accommodation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AccommodationService {
    Page<Accommodation> getAll(Pageable pageable);
    Optional<Accommodation> getById(String accommodationId);
    Accommodation create(Accommodation accommodation) throws AppException;
    Accommodation update (Accommodation accommodation);
    void delete(String accommodationId);
}
