package com.gmail.llemaxiss.app.common.model.response;

import com.gmail.llemaxiss.app.common.enums.ErrorCode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Wrapper for the result of a business operation
 *
 * <p>
 * Contains either successful data or a list of errors with their codes.
 * </p>
 *
 * @param <D> the type of the successful result data
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OperationResult<D> {
  
  private final D data;
  
  private final List<ErrorDetail> errors;
  
  private final boolean success;
  
  /**
   * Creates a successful result without data.
   *
   * @param <T>  the type of the data
   *
   * @return successful {@link OperationResult}
   */
  @NotNull
  public static <T> OperationResult<T> success() {
    return new OperationResult<>(null, null, true);
  }
  
  /**
   * Creates a successful result with data.
   *
   * @param data the result data
   * @param <T>  the type of the data
   *
   * @return successful {@link OperationResult}
   */
  @NotNull
  public static <T> OperationResult<T> success(T data) {
    return new OperationResult<>(data, null, true);
  }
  
  /**
   * Creates a failed result with a single error.
   *
   * @param errorCode the error code
   * @param message   the error message
   *
   * @param <T>       the type of the data
   *
   * @return failed {@link OperationResult}
   */
  @NotNull
  public static <T> OperationResult<T> failure(@NotNull ErrorCode errorCode, String message) {
    List<ErrorDetail> errorDetails = List.of(
      new ErrorDetail(errorCode, message)
    );
    
    return new OperationResult<>(null, errorDetails, false);
  }
  
  /**
   * Creates a failed result with multiple errors.
   *
   * @param errors the list of error details
   * @param <T>    the type of the data
   *
   * @return failed {@link OperationResult}
   */
  @NotNull
  public static <T> OperationResult<T> failure(@NotEmpty List<ErrorDetail> errors) {
    return new OperationResult<>(null, errors, false);
  }
  
  /**
   * Represents a single error detail.
   */
  @AllArgsConstructor
  @Getter
  public static final class ErrorDetail {
    
    @NotNull
    private final ErrorCode errorCode;
    
    private final String message;
    
  }
  
}
