package com.treetoplodge.treetoplodge_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom application exception that provides structured error information
 * with error codes and HTTP status codes.
 */
@Getter
public class AppException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Object details;

    /**
     * Creates a basic internal server error exception
     * @param message The error message
     */
    public AppException(String message) {
        super(message);
        this.errorCode = "ERR-500";
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.details = null;
    }

    /**
     * Creates an exception with a specific error code and HTTP status
     * @param message The error message
     * @param errorCode Custom error code
     * @param httpStatus HTTP status to return
     */
    public AppException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = null;
    }

    /**
     * Creates an exception with a specific error code, HTTP status and additional details
     * @param message The error message
     * @param errorCode Custom error code
     * @param httpStatus HTTP status to return
     * @param details Additional error details (can be structured data)
     */
    public AppException(String message, String errorCode, HttpStatus httpStatus, Object details) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = details;
    }

    /**
     * Creates an exception from a cause with a specific error code and HTTP status
     * @param message The error message
     * @param cause The original exception
     * @param errorCode Custom error code
     * @param httpStatus HTTP status to return
     */
    public AppException(String message, Throwable cause, String errorCode, HttpStatus httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = null;
    }

    /**
     * Creates a 404 Not Found exception
     * @param message The error message
     * @return AppException with 404 status
     */
    public static AppException notFound(String message) {
        return new AppException(message, "ERR-404", HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a 404 Not Found exception with entity type and ID
     * @param entityType The type of entity that was not found (e.g., "User", "Product")
     * @param id The ID that was searched for
     * @return AppException with 404 status
     */
    public static AppException notFound(String entityType, Object id) {
        return new AppException(
            String.format("%s with ID %s not found", entityType, id),
            "ERR-404", 
            HttpStatus.NOT_FOUND
        );
    }

    /**
     * Creates a 400 Bad Request exception
     * @param message The error message
     * @return AppException with 400 status
     */
    public static AppException badRequest(String message) {
        return new AppException(message, "ERR-400", HttpStatus.BAD_REQUEST);
    }

    /**
     * Creates a 400 Bad Request exception with validation details
     * @param message The error message
     * @param details Validation error details
     * @return AppException with 400 status and details
     */
    public static AppException badRequest(String message, Object details) {
        return new AppException(message, "ERR-400", HttpStatus.BAD_REQUEST, details);
    }

    /**
     * Creates a 401 Unauthorized exception
     * @param message The error message
     * @return AppException with 401 status
     */
    public static AppException unauthorized(String message) {
        return new AppException(message, "ERR-401", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Creates a 403 Forbidden exception
     * @param message The error message
     * @return AppException with 403 status
     */
    public static AppException forbidden(String message) {
        return new AppException(message, "ERR-403", HttpStatus.FORBIDDEN);
    }

    /**
     * Creates a 409 Conflict exception
     * @param message The error message
     * @return AppException with 409 status
     */
    public static AppException conflict(String message) {
        return new AppException(message, "ERR-409", HttpStatus.CONFLICT);
    }
    
    /**
     * Creates a 500 Internal Server Error exception from another exception
     * @param message The error message
     * @param cause The original exception
     * @return AppException with 500 status
     */
    public static AppException internalError(String message, Throwable cause) {
        return new AppException(message, cause, "ERR-500", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a 503 Service Unavailable exception
     * @param message The error message
     * @return AppException with 503 status
     */
    public static AppException serviceUnavailable(String message) {
        return new AppException(message, "ERR-503", HttpStatus.SERVICE_UNAVAILABLE);
    }
}