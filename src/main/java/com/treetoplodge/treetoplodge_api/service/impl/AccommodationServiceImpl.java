package com.treetoplodge.treetoplodge_api.service.impl;

import com.treetoplodge.treetoplodge_api.service.AccommodationService;
import com.treetoplodge.treetoplodge_api.exception.AppException;
import com.treetoplodge.treetoplodge_api.model.Accommodation;
import com.treetoplodge.treetoplodge_api.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository repository;
    private final FileUploadServiceImpl fileUploadService;

    @Override
    public Page<Accommodation> getAll(Pageable pageable) {
        return repository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        ));
    }

    @Override
    public Optional<Accommodation> getById(String accommodationId) {
        var existingData = repository.findByAccommodationId(accommodationId);
        if(existingData.isEmpty()){
            throw AppException.notFound("Not found");
        }
        return repository.findByAccommodationId(accommodationId);
    }

    @Transactional
    @Override
    public Accommodation create(Accommodation accommodation) throws AppException {
        // if(accommodation.getAccommodationId().isEmpty() || accommodation.getAccommodationId() == ""){
        //     accommodation.setAccommodationId(generateUniqueAccommodationId());
        // }
        accommodation.setCreatedAt(LocalDateTime.now());
        return repository.save(accommodation);
    }

    @Transactional
    @Override
    public Accommodation update(Accommodation accommodation) throws AppException{
        Optional<Accommodation> existingDataOpt = repository.findByAccommodationId(accommodation.getAccommodationId());
        if(existingDataOpt.isEmpty()){
            throw AppException.notFound("Not found");
        }

        Accommodation existingData = existingDataOpt.get();

        // Copy properties from input to existing entity
        existingData.setName(accommodation.getName());
        existingData.setType(accommodation.getType());
        existingData.setDescription(accommodation.getDescription());
        existingData.setMainImgUrl(accommodation.getMainImgUrl());
        existingData.setAdditionalImgUrls(accommodation.getAdditionalImgUrls());
        existingData.setPricePerNight(accommodation.getPricePerNight());
        existingData.setMaxGuests(accommodation.getMaxGuests());
        existingData.setBeds(accommodation.getBeds());
        existingData.setBathrooms(accommodation.getBathrooms());
        existingData.setFeatures(accommodation.getFeatures());

        // Set metadata
        existingData.setUpdatedAt(LocalDateTime.now());
        existingData.setUpdatedBy(accommodation.getUpdatedBy());

        return repository.save(existingData);
    }

    @Transactional
    @Override
    public void delete(String accommodationId) throws AppException {
        log.debug("Deleting accommodation with ID: {}", accommodationId);

        Accommodation accommodation = repository.findByAccommodationId(accommodationId)
                .orElseThrow(() -> {
                    log.error("Accommodation with ID: {} not found for deletion", accommodationId);
                    return AppException.notFound("Accommodation", accommodationId);
                });
        try {
            // Delete main image if it exists
            if (accommodation.getMainImgUrl() != null && !accommodation.getMainImgUrl().isEmpty()) {
                log.debug("Deleting main image for accommodation ID: {}", accommodationId);
                try {
                    fileUploadService.deleteFile(accommodation.getMainImgUrl());
                } catch (Exception e) {
                    log.warn("Failed to delete main image for accommodation ID: {}, continuing with deletion", accommodationId, e);
                }
            }

            // Delete additional images if they exist
            if (accommodation.getAdditionalImgUrls() != null && !accommodation.getAdditionalImgUrls().isEmpty()) {
                log.debug("Deleting {} additional images for accommodation ID: {}",
                        accommodation.getAdditionalImgUrls().size(), accommodationId);
                try {
                    fileUploadService.deleteFiles(accommodation.getAdditionalImgUrls());
                } catch (Exception e) {
                    log.warn("Failed to delete additional images for accommodation ID: {}, continuing with deletion", accommodationId, e);
                }
            }

            // Delete the accommodation record
            repository.deleteByAccommodationId(accommodationId);
            log.info("Successfully deleted accommodation with ID: {}", accommodationId);
        } catch (Exception e) {
            log.error("Error deleting accommodation with ID: {}", accommodationId, e);
            throw AppException.internalError("Failed to delete accommodation: " + e.getMessage(), e);
        }
    }

}