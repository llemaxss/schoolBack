package com.gmail.llemaxiss.app._common.exception;

import com.gmail.llemaxiss.app._common.enums.ErrorCode;
import com.gmail.llemaxiss.app._common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app._common.model.response.ErrorResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Global exception handler that converts exceptions to {@link ErrorResponse}
 *
 * <p>
 * This handler catches all exceptions thrown during request processing and converts
 * them into a standardized {@link ErrorResponse} with appropriate HTTP status codes.
 * </p>
 *
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  
  /**
   * Handles {@link CommonException} thrown by service methods
   *
   * @param ex the application exception
   *
   * @return response with concrete {@link ErrorCode}
   */
  @ExceptionHandler(CommonException.class)
  public ResponseEntity<ErrorResponse> handleCommonException(@NotNull CommonException ex) {
    log.warn("Business error: {} - {}", ex.getErrorCode(), ex.getMessage());
    
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(
        ErrorResponse.of(
          ex.getErrorCode(),
          ex.getMessage()
        )
      );
  }
  
  /**
   * Handles validation errors from {@code @Valid} annotations on DTO models
   *
   * @param ex the validation exception
   * @return response with {@link ErrorCode#VALIDATION_FAILED}
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(@NotNull MethodArgumentNotValidException ex) {
    List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
      .getFieldErrors()
      .stream()
      .map(error ->
        new ErrorResponse.FieldError(
          error.getField(),
          error.getCode(),
          error.getDefaultMessage()
        )
      )
      .toList();
    
    log.warn("Validation failed: {}", fieldErrors);
    
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(
        ErrorResponse.of(
          ErrorCode.VALIDATION_FAILED,
          "Validation failed",
          fieldErrors)
      );
  }
  
  /**
   * Handles authentication errors (bad credentials, disabled or locked account)
   *
   * <p>
   * The message is intentionally generic to not reveal the reason (does the user exist, is it active, etc.).
   * </p>
   *
   * @param ex the authentication exception
   * @return response with {@link ErrorCode#AUTHENTICATION_FAILED}
   */
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationException(@NotNull AuthenticationException ex) {
    log.warn("Authentication failed: {}", ex.getMessage());
    
    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(
        ErrorResponse.of(
          ErrorCode.AUTHENTICATION_FAILED,
          "Authentication failed"
        )
      );
  }
  
  /**
   * Handles access denied errors thrown by method security ({@code @PreAuthorize})
   *
   * @param ex the access denied exception
   * @return response with {@link ErrorCode#ACCESS_DENIED}
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(@NotNull AccessDeniedException ex) {
    log.warn("Access denied: {}", ex.getMessage());
    
    return ResponseEntity
      .status(HttpStatus.FORBIDDEN)
      .body(
        ErrorResponse.of(
          ErrorCode.ACCESS_DENIED,
          "Access denied"
        )
      );
  }
  
  /**
   * Handles all other unexpected exceptions
   *
   * @param ex the unexpected exception
   * @return response with {@link ErrorCode#INTERNAL_ERROR}
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(@NotNull Exception ex) {
    log.error("Unexpected error", ex);
    
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(
        ErrorResponse.of(
          ErrorCode.INTERNAL_ERROR,
          "An unexpected error occurred"
        )
      );
  }
  
}
