package com.treetoplodge.treetoplodge_api.service.impl;

import com.treetoplodge.treetoplodge_api.service.FoodBeverageService;
import com.treetoplodge.treetoplodge_api.exception.AppException;
import com.treetoplodge.treetoplodge_api.model.FoodBeverage;
import com.treetoplodge.treetoplodge_api.repository.FoodBeverageRepository;
import com.treetoplodge.treetoplodge_api.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.treetoplodge.treetoplodge_api.util.Generator.generateUniqueFoodBeverageCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodBeverageServiceImpl implements FoodBeverageService {
    private final FoodBeverageRepository repository;
    private final Validator validator;

    @Override
    public Page<FoodBeverage> getAll(Pageable pageable) throws AppException {
        log.debug("Fetching all with pagination");
        Page<FoodBeverage> foodBeverages = repository.findAll(pageable);
        log.debug("Retrieved {}",foodBeverages.getContent());
        return foodBeverages;
    }

    @Override
    public Optional<FoodBeverage> getById(String code) throws AppException {
        var existingData = repository.findByCode(code);
        if(existingData.isEmpty()) throw AppException.notFound("Not found");
        return existingData;
    }

    @Transactional
    @Override
    public FoodBeverage create(FoodBeverage foodBeverage) throws AppException {
        validator.validateFoodBeverageCode(foodBeverage.getCode());
        if(foodBeverage.getCode().isEmpty()){
            foodBeverage.setCode(generateUniqueFoodBeverageCode());
        }
        foodBeverage.setCreatedAt(LocalDateTime.now());
        try {
            log.debug("Save food beverage success");
            return repository.save(foodBeverage);
        }catch (Exception e){
            log.error("Failed to create food beverage");
            throw AppException.internalError("Failed to create food beverage",e.getCause());
        }
    }

    @Transactional
    @Override
    public FoodBeverage update(FoodBeverage foodBeverage) throws AppException {
        FoodBeverage existingItem = repository.findByCode(foodBeverage.getCode())
                .orElseThrow(() -> {
                    log.error("FoodBeverage with code {} not found", foodBeverage.getCode());
                    return AppException.notFound("Food/Beverage not found");
                });

        // Copy all relevant properties
        existingItem.setName(foodBeverage.getName());
        existingItem.setDescription(foodBeverage.getDescription());
        existingItem.setPrice(foodBeverage.getPrice());
        existingItem.setMainImgUrl(foodBeverage.getMainImgUrl());
        existingItem.setAdditionalImageUrls(foodBeverage.getAdditionalImageUrls());
        existingItem.setCategory(foodBeverage.getCategory());
        existingItem.setRating(foodBeverage.getRating());
        existingItem.setReview(foodBeverage.getReview());

        // Update metadata
        existingItem.setUpdatedAt(LocalDateTime.now());
        existingItem.setUpdatedBy(foodBeverage.getUpdatedBy());

        return repository.save(existingItem);
    }

    @Override
    public void delete(String code) throws AppException {
        var existingData = repository.existsFoodBeverageByCode(code);
        if(!existingData) throw AppException.notFound("Not found");
        repository.deleteByCode(code);
    }
}
