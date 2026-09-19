package com.gmail.llemaxiss.app.common.model.response;

import com.gmail.llemaxiss.app.common.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Standard error response for API errors
 *
 * <p>
 * Used by {@code @ControllerAdvice} to return structured error information to the client.
 * </p>
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
  
  private ErrorCode errorCode;
  
  private String message;
  
  private List<FieldError> fieldErrors;
  
  public static ErrorResponse of(ErrorCode errorCode, String message) {
    return new ErrorResponse(errorCode, message, null);
  }
  
  public static ErrorResponse of(ErrorCode errorCode, String message, List<FieldError> fieldErrors) {
    return new ErrorResponse(errorCode, message, fieldErrors);
  }
  
  /**
   * Represents a field-level validation error
   *
   * <p>
   * The {@code code} field contains the validation constraint name
   * (e.g., "NotBlank", "Size", "Email") from Spring Bean Validation.
   * </p>
   *
   */
  @Getter
  @Setter
  @AllArgsConstructor
  public static class FieldError {
    
    private String field;
    
    private String code;
    
    private String message;
    
  }
  
}
