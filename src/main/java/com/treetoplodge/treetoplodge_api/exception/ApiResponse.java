package com.treetoplodge.treetoplodge_api.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse {
    /**
     * Creates a success response with data
     */
    public static ResponseEntity<Object> success(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Operation completed successfully");
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

     public static ResponseEntity<Object> success(String data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Operation completed successfully");
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<Object> success() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Operation completed successfully");
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    /**
     * Creates a success response for resource creation
     */
    public static ResponseEntity<Object> created(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Resource created successfully");
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Creates a success response for resource updates
     */
    public static ResponseEntity<Object> updated(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Resource updated successfully");
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    /**
     * Creates an error response from an AppException
     */
    public static ResponseEntity<Object> error(AppException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", e.getMessage());
        response.put("timestamp", LocalDateTime.now());

        if (e.getErrorCode() != null) {
            response.put("errorCode", e.getErrorCode());
        }

        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    /**
     * Creates a generic error response
     */
    public static ResponseEntity<Object> error(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Creates a response for a generic exception
     */
    public static ResponseEntity<Object> error(Exception e) {
        return error("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a paginated response
     */
    public static ResponseEntity<Object> paged(Page<?> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Operation completed successfully");
        response.put("content", page.getContent());
        response.put("totalPages", page.getTotalPages());
        response.put("totalElements", page.getTotalElements());
        response.put("number", page.getNumber());
        response.put("size", page.getSize());
        response.put("first", page.isFirst());
        response.put("last", page.isLast());
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    /**
     * Creates a not found response
     */
    public static ResponseEntity<Object> notFound(String message) {
        return error(message, HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a bad request response
     */
    public static ResponseEntity<Object> badRequest(String message) {
        return error(message, HttpStatus.BAD_REQUEST);
    }

    /**
    * Creates a bad request response with validation errors
    */
    public static ResponseEntity<Object> badRequest(String message, Map<String, String> validationErrors) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("data", validationErrors); 
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
