package com.treetoplodge.treetoplodge_api.util;

import com.treetoplodge.treetoplodge_api.exception.AppException;
import com.treetoplodge.treetoplodge_api.repository.AccommodationRepository;
import com.treetoplodge.treetoplodge_api.repository.FoodBeverageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class Validator {
    private final AccommodationRepository repository;
    private final FoodBeverageRepository foodBeverageRepository;

    public void validateAccommodationId(String accommodationId) {
        if (repository.existsAccommodationsByAccommodationId(accommodationId)) {
            log.error("AccommodationId is taken, Choose another or keep blank, system will auto generate unique accommodation id for you.");
            throw AppException.badRequest("AccommodationId is taken, Choose another or keep blank, system will auto generate unique accommodation id for you.");
        }
    }

    public void validateFoodBeverageCode(String code){
        if(code != null){
            if(foodBeverageRepository.existsFoodBeverageByCode(code)){
                log.error("FoodBeverage Code is token, Choose another or keep blank, system will auto generate unique code for you.");
                throw AppException.badRequest("FoodBeverage Code is token, Choose another or keep blank, system will auto generate unique code for you.");
            }
        }
    }
}
