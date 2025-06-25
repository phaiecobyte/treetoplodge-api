package com.treetoplodge.treetoplodge_api.controller;

import com.treetoplodge.treetoplodge_api.service.impl.AccommodationServiceImpl;
import com.treetoplodge.treetoplodge_api.exception.ApiResponse;
import com.treetoplodge.treetoplodge_api.exception.AppException;
import com.treetoplodge.treetoplodge_api.model.Accommodation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Slf4j
@RestController
@RequestMapping("api/v1/accommodations")
@RequiredArgsConstructor
public class AccommodationController {
    private static final String LOG_REQUEST_START ="Requesting {} operation for food-beverage";
    private static final String LOG_REQUEST_SUCCESS="Successfully completed {} operation for food-beverage";
    private static final String LOG_REQUEST_APP_ERROR="Application error during {} operation:{}";
    private static final String LOG_REQUEST_UNEXPECTED_ERROR = "Unexpected error during {} operation:{}";
    private static final String LOG_REQUEST_COMPLETED = "Completed {} operation";
    private final AccommodationServiceImpl service;

   @PostMapping("/test")
//    @PreAuthorize("hasAnyRole('ADMIN')")
   public String postMethodName(@RequestBody String entity) {   
       return "Hello spring boot";
   }
   
    

    @GetMapping
    public ResponseEntity<Object> getAll(Pageable pageable) {
        log.info(LOG_REQUEST_START,"getAll");
        try {
            log.info(LOG_REQUEST_SUCCESS,"getAll");
            return ApiResponse.paged(service.getAll(pageable));
        }catch (AppException e){
            log.error(LOG_REQUEST_APP_ERROR,"getAll",e.getMessage());
            return ApiResponse.error(e);
        }catch (Exception e){
            log.error(LOG_REQUEST_UNEXPECTED_ERROR,"getAll",e.getMessage());
            return ApiResponse.error(e);
        }finally {
            log.info(LOG_REQUEST_COMPLETED,"getAll");
        }
    }

    @GetMapping("{accommodationId}")
    public ResponseEntity<Object> getById(@PathVariable String accommodationId) {
        log.info(LOG_REQUEST_START,"getById");
        try {
            log.info(LOG_REQUEST_SUCCESS,"getById");
            return ApiResponse.success(service.getById(accommodationId));
        }catch (AppException e){
            log.error(LOG_REQUEST_APP_ERROR,"getById",e.getMessage());
            return ApiResponse.error(e);
        }catch (Exception e){
            log.error(LOG_REQUEST_UNEXPECTED_ERROR,"getById",e.getMessage());
            return ApiResponse.error(e);
        }finally {
            log.info(LOG_REQUEST_COMPLETED,"getById");
        }
    }

    @PostMapping
    // @PreAuthorize("hasAnyRole('SHOP','ADMIN')")
    public ResponseEntity<Object> create(@RequestBody @Valid Accommodation accommodation) {
        log.info(LOG_REQUEST_START,"create");
        try {
            log.info(LOG_REQUEST_SUCCESS,"create");
            return ApiResponse.success(service.create(accommodation));
        } catch (AppException e) {
            log.error(LOG_REQUEST_APP_ERROR,"create",e.getMessage());
            return ApiResponse.error(e);
        } catch (Exception e) {
            log.error(LOG_REQUEST_UNEXPECTED_ERROR,"create",e.getMessage());
            return ApiResponse.error(e);
        } finally {
            log.info(LOG_REQUEST_COMPLETED,"create");
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('SHOP') or hasRole('ADMIN')")
    public ResponseEntity<Object> update(@RequestBody @Valid Accommodation accommodation) {
        log.info(LOG_REQUEST_START,"update");
        try {
            log.info(LOG_REQUEST_SUCCESS,"update");
            return ApiResponse.updated(service.update(accommodation));
        }catch (AppException e){
            log.error(LOG_REQUEST_APP_ERROR,"update",e.getMessage());
            return ApiResponse.error(e);
        }catch (Exception e){
            log.error(LOG_REQUEST_UNEXPECTED_ERROR,"update",e.getMessage());
            return ApiResponse.error(e);
        }finally {
            log.info(LOG_REQUEST_COMPLETED,"update");
        }
    }

    @DeleteMapping("{accommodationId}")
    @PreAuthorize("hasRole('SHOP') or hasRole('ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable String accommodationId) {
        log.info(LOG_REQUEST_START,"delete");
        try {
            log.info(LOG_REQUEST_SUCCESS,"delete");
            service.delete(accommodationId);
            return ApiResponse.success();
        }catch (AppException e){
            log.error(LOG_REQUEST_APP_ERROR,"delete",e.getMessage());
            return ApiResponse.error(e);
        }catch (Exception e){
            log.error(LOG_REQUEST_APP_ERROR,"delete",e.getMessage());
            return ApiResponse.error(e);
        }finally {
            log.info(LOG_REQUEST_COMPLETED,"delete");
        }
    }
}