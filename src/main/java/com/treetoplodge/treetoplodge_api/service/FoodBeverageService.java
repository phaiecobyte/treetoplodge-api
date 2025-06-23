package com.treetoplodge.treetoplodge_api.Service;

import com.treetoplodge.treetoplodge_api.exception.AppException;
import com.treetoplodge.treetoplodge_api.model.FoodBeverage;
import org.hibernate.metamodel.mapping.ForeignKeyDescriptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FoodBeverageService {
    Page<FoodBeverage> getAll(Pageable pageable) throws AppException;
    Optional<FoodBeverage> getById(String code) throws AppException;
    FoodBeverage create(FoodBeverage foodBeverage) throws AppException;
    FoodBeverage update(FoodBeverage foodBeverage) throws AppException;
    void delete(String code) throws AppException;
}
