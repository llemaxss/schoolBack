package com.gmail.llemaxiss.app.common.model.response;

import com.gmail.llemaxiss.app.common.enums.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Standard error response structure for API failures")
public class ErrorResponse {
  
  @Schema(description = "Unique error code for frontend localization", example = "ROLE_NOT_FOUND")
  private ErrorCode errorCode;
  
  @Schema(description = "Human-readable error message", example = "Role with id 123 not found")
  private String message;
  
  @Schema(description = "List of field-specific validation errors")
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
  @Schema(description = "Details of a specific field validation failure")
  public static class FieldError {
    
    @Schema(description = "Name of the invalid field", example = "firstName")
    private String field;
    
    @Schema(description = "Validation constraint that failed", example = "NotEmpty")
    private String code;
    
    @Schema(description = "Default error message", example = "must not be empty")
    private String message;
    
  }
  
}
