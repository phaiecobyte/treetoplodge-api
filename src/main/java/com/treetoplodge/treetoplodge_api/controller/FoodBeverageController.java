package com.treetoplodge.treetoplodge_api.controller;

import com.treetoplodge.treetoplodge_api.service.impl.FoodBeverageServiceImpl;
import com.treetoplodge.treetoplodge_api.exception.ApiResponse;
import com.treetoplodge.treetoplodge_api.exception.AppException;
import com.treetoplodge.treetoplodge_api.model.FoodBeverage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("api/v1/food-beverages")
@RequiredArgsConstructor
public class FoodBeverageController {
    private static final String LOG_REQUEST_START ="Requesting {} operation for food-beverage";
    private static final String LOG_REQUEST_SUCCESS="Successfully completed {} operation for food-beverage";
    private static final String LOG_REQUEST_APP_ERROR="Application error during {} operation:{}";
    private static final String LOG_REQUEST_UNEXPECTED_ERROR = "Unexpected error during {} operation:{}";
    private static final String LOG_REQUEST_COMPLETED = "Completed {} operation";

    private final FoodBeverageServiceImpl service;

    @GetMapping
    public ResponseEntity<Object> getAll(Pageable pageable){
        log.info(LOG_REQUEST_START,"getAll");
        try {
            log.info(LOG_REQUEST_SUCCESS,"getAll");
            return ApiResponse.paged(service.getAll(pageable));
        }catch (AppException e){
            log.error(LOG_REQUEST_APP_ERROR, e.getMessage(),"getAll");
            return ApiResponse.error(e);
        }catch (Exception e){
            log.error(LOG_REQUEST_UNEXPECTED_ERROR,e.getMessage(),"getAll");
            return ApiResponse.error(e);
        }finally {
            log.info(LOG_REQUEST_COMPLETED,"getAll");
        }
    }

    @GetMapping("{code}")
    public ResponseEntity<Object> getByCode(@PathVariable String code){
        log.info(LOG_REQUEST_START,"getByCode");
        try {
            log.info(LOG_REQUEST_SUCCESS,"getByCode");
            return ApiResponse.success(service.getById(code));
        }catch (AppException e){
            log.error(LOG_REQUEST_APP_ERROR, e.getMessage(),"getByCode");
            return ApiResponse.error(e);
        }catch (Exception e){
            log.error(LOG_REQUEST_UNEXPECTED_ERROR, e.getMessage(),"getByCode");
            return ApiResponse.error(e);
        }finally {
            log.info(LOG_REQUEST_COMPLETED,"getByCode");
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('SHOP') or hasRole('ADMIN')")
    public ResponseEntity<Object> create(@RequestBody @Valid FoodBeverage foodBeverage) {
        log.info(LOG_REQUEST_START,"create");
        try {
            FoodBeverage data = service.create(foodBeverage);
            log.info(LOG_REQUEST_SUCCESS,"create");
            return ApiResponse.success(data);
        } catch (AppException e) {
            log.error(LOG_REQUEST_APP_ERROR, e.getMessage(), "create");
            return ApiResponse.error(e);
        } catch (Exception e) {
            log.error(LOG_REQUEST_UNEXPECTED_ERROR,"create",e.getMessage());
            return ApiResponse.error(e);
        }finally {
            log.info(LOG_REQUEST_COMPLETED,"create");
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('SHOP') or hasRole('ADMIN')")
    public ResponseEntity<Object> update (@RequestBody @Valid FoodBeverage foodBeverage){
        log.info(LOG_REQUEST_START,"update");
        try {
            log.info(LOG_REQUEST_SUCCESS,"update");
            return ApiResponse.updated(service.update(foodBeverage));
        }catch (AppException e){
            log.error(LOG_REQUEST_APP_ERROR,"update", e.getMessage());
            return ApiResponse.error(e);
        }catch (Exception e){
            log.error(LOG_REQUEST_UNEXPECTED_ERROR,"update",e.getMessage());
            return ApiResponse.error(e);
        }finally {
            log.info(LOG_REQUEST_COMPLETED,"update");
        }
    }

    @DeleteMapping("{code}")
    @PreAuthorize("hasRole('SHOP' or hasRole('ADMIN'))")
    public ResponseEntity<Object> delete(@PathVariable String code){
        log.info(LOG_REQUEST_START,"delete");
        try {
            log.info(LOG_REQUEST_SUCCESS,"delete");
            service.delete(code);
            return ApiResponse.success();
        }catch (AppException e){
            log.error(LOG_REQUEST_APP_ERROR,"delete",e.getMessage());
            return ApiResponse.error(e);
        }catch (Exception e){
            log.error(LOG_REQUEST_UNEXPECTED_ERROR,"update",e.getMessage());
            return ApiResponse.error(e);
        }finally {
            log.error(LOG_REQUEST_COMPLETED,"update");
        }
    }
}