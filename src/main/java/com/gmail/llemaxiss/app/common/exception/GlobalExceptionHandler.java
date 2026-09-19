package com.gmail.llemaxiss.app.common.exception;

import com.gmail.llemaxiss.app.common.enums.ErrorCode;
import com.gmail.llemaxiss.app.common.exception.model.CommonException;
import com.gmail.llemaxiss.app.common.model.response.OperationResult;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Global exception handler that converts exceptions to {@link OperationResult} responses
 *
 * <p>
 * This handler catches all exceptions thrown during request processing and converts
 * them into a standardized {@link OperationResult} response with appropriate HTTP status codes.
 * </p>
 *
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  
  /**
   * Handles {@link CommonException} thrown by service methods.
   *
   * @param ex the application exception
   *
   * @return response with error code and message
   */
  @ExceptionHandler(CommonException.class)
  public ResponseEntity<OperationResult<Void>> handleCommonException(@NotNull CommonException ex) {
    log.warn("Business error: {} - {}", ex.getErrorCode(), ex.getMessage());
    
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(
        OperationResult.failure(
          ex.getErrorCode(),
          ex.getMessage()
        )
      );
  }
  
  /**
   * Handles validation errors from {@code @Valid} annotations on DTO models.
   *
   * @param ex the validation exception
   * @return response with validation error details
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<OperationResult<Void>> handleValidationException(@NotNull MethodArgumentNotValidException ex) {
    List<OperationResult.ErrorDetail> errors = ex.getBindingResult()
      .getFieldErrors()
      .stream()
      .map(error -> new OperationResult.ErrorDetail(
        ErrorCode.VALIDATION_FAILED,
        error.getField() + ": " + error.getDefaultMessage()
      ))
      .toList();
    
    log.warn("Validation failed: {}", errors);
    
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(
        OperationResult.failure(errors)
      );
  }
  
  /**
   * Handles all other unexpected exceptions.
   *
   * @param ex the unexpected exception
   * @return response with internal error code
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<OperationResult<Void>> handleGenericException(@NotNull Exception ex) {
    log.error("Unexpected error", ex);
    
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(
        OperationResult.failure(
          ErrorCode.INTERNAL_ERROR,
          "An unexpected error occurred"
        )
      );
  }
  
}
