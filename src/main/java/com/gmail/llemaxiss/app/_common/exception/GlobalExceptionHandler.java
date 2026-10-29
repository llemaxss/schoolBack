package com.gmail.llemaxiss.app._common.exception;

import com.gmail.llemaxiss.app._common.enums.ErrorCode;
import com.gmail.llemaxiss.app._common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app._common.model.response.ErrorResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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
          fieldErrors
        )
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
  
  /**
   * Handles validation errors of method parameters ({@code @NotNull} on {@code @PathVariable}, {@code @RequestParam})
   *
   * @param ex the method validation exception
   * @return response with {@link ErrorCode#VALIDATION_FAILED}
   */
  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<ErrorResponse> handleMethodValidationException(@NotNull HandlerMethodValidationException ex) {
    List<ErrorResponse.FieldError> fieldErrors = ex.getParameterValidationResults()
      .stream()
      .flatMap(result ->
        result.getResolvableErrors()
          .stream()
          .map(error ->
            new ErrorResponse.FieldError(
              result.getMethodParameter()
                .getParameterName(),
              getConstraintCode(error),
              error.getDefaultMessage()
            )
          )
      )
      .toList();
    
    log.warn("Method parameter validation failed: {}", fieldErrors);
    
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(
        ErrorResponse.of(
          ErrorCode.VALIDATION_FAILED,
          "Validation failed",
          fieldErrors
        )
      );
  }
  
  /**
   * Handles a missing required request parameter
   *
   * @param ex the missing parameter exception
   * @return response with {@link ErrorCode#VALIDATION_FAILED}
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingParameterException(
    @NotNull MissingServletRequestParameterException ex
  ) {
    log.warn("Missing request parameter: {}", ex.getParameterName());
    
    List<ErrorResponse.FieldError> fieldErrors = List.of(
      new ErrorResponse.FieldError(
        ex.getParameterName(),
        "Missing",
        "Required parameter is missing"
      )
    );
    
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(
        ErrorResponse.of(
          ErrorCode.VALIDATION_FAILED,
          "Validation failed",
          fieldErrors
        )
      );
  }
  
  /**
   * Handles a parameter of the wrong type (for example, not a UUID in the path)
   *
   * @param ex the type mismatch exception
   * @return response with {@link ErrorCode#VALIDATION_FAILED}
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatchException(@NotNull MethodArgumentTypeMismatchException ex) {
    log.warn("Parameter type mismatch: {}", ex.getName());
    
    List<ErrorResponse.FieldError> fieldErrors = List.of(
      new ErrorResponse.FieldError(
        ex.getName(),
        "TypeMismatch",
        "Parameter has invalid format"
      )
    );
    
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(
        ErrorResponse.of(
          ErrorCode.VALIDATION_FAILED,
          "Validation failed",
          fieldErrors
        )
      );
  }
  
  /**
   * Handles an unreadable request body (malformed JSON, unknown enum value, wrong field type)
   *
   * <p>
   * The exception message is not returned to the client: it may contain internal details.
   * </p>
   *
   * @param ex the unreadable message exception
   * @return response with {@link ErrorCode#BAD_REQUEST}
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleNotReadableException(@NotNull HttpMessageNotReadableException ex) {
    log.warn("Request body is not readable: {}", ex.getMessage());
    
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(
        ErrorResponse.of(
          ErrorCode.BAD_REQUEST,
          "Request body is malformed or has invalid values"
        )
      );
  }
  
  /**
   * Handles requests with an unsupported HTTP method
   *
   * @param ex the method not supported exception
   * @return response with {@link ErrorCode#METHOD_NOT_ALLOWED}
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(
    @NotNull HttpRequestMethodNotSupportedException ex
  ) {
    log.warn("Method not supported: {}", ex.getMessage());
    
    return ResponseEntity
      .status(HttpStatus.METHOD_NOT_ALLOWED)
      .body(
        ErrorResponse.of(
          ErrorCode.METHOD_NOT_ALLOWED,
          "HTTP method is not supported for this endpoint"
        )
      );
  }
  
  /**
   * Handles requests with an unsupported Content-Type
   *
   * @param ex the media type not supported exception
   * @return response with {@link ErrorCode#UNSUPPORTED_MEDIA_TYPE}
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleMediaTypeNotSupportedException(
    @NotNull HttpMediaTypeNotSupportedException ex
  ) {
    log.warn("Media type not supported: {}", ex.getMessage());
    
    return ResponseEntity
      .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
      .body(
        ErrorResponse.of(
          ErrorCode.UNSUPPORTED_MEDIA_TYPE,
          "Content type is not supported"
        )
      );
  }
  
  /**
   * Handles requests to a non-existent URL
   *
   * @param ex the no resource found exception
   * @return response with {@link ErrorCode#NOT_FOUND}
   */
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoResourceFoundException(@NotNull NoResourceFoundException ex) {
    log.warn("Resource not found: {}", ex.getResourcePath());
    
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(
        ErrorResponse.of(
          ErrorCode.NOT_FOUND,
          "Resource not found"
        )
      );
  }
  
  /**
   * Takes the constraint name (for example, "NotNull") from the validation error codes
   *
   * <p>
   * The codes go from the most specific to the most general, so the last one is the constraint name.
   * </p>
   */
  private String getConstraintCode(@NotNull MessageSourceResolvable error) {
    String[] codes = error.getCodes();
    
    if (codes == null || ArrayUtils.isEmpty(codes)) {
      return null;
    }
    
    return codes[codes.length - 1];
  }
  
}
