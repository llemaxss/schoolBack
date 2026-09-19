package com.gmail.llemaxiss.app.common.exception.model.response;

import com.gmail.llemaxiss.app.common.enums.ErrorCode;
import lombok.Getter;

/**
 * Base application exception for business logic errors
 *
 * <p>
 * It carries an {@link ErrorCode} that can be used
 * by the frontend to display localized error messages.
 * </p>
 *
 * <p>
 * The {@code @ControllerAdvice} catches this exception and converts it to
 * an {@code OperationResult} response for the client.
 * </p>
 *
 */
@Getter
public class CommonException extends RuntimeException {
  
  private final ErrorCode errorCode;
  
  public CommonException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
  
  public CommonException(ErrorCode errorCode, String message) {
    super(message);
    
    this.errorCode = errorCode;
  }
  
  public CommonException(ErrorCode errorCode, Throwable cause) {
    super(cause);
    
    this.errorCode = errorCode;
  }
  
  public CommonException(ErrorCode errorCode, Throwable cause, String message) {
    super(message, cause);
    
    this.errorCode = errorCode;
  }
  
}
