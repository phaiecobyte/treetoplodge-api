package com.treetoplodge.treetoplodge_api.repository;

import com.treetoplodge.treetoplodge_api.model.FoodBeverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodBeverageRepository extends JpaRepository<FoodBeverage,Long> {
    boolean existsFoodBeverageByCode(String code);

    Optional<FoodBeverage> findByCode(String code);

    void deleteByCode(String code);
}
