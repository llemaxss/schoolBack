package com.gmail.llemaxiss.app.unit._common;

import com.gmail.llemaxiss.app._common.component.AppProperty;
import com.gmail.llemaxiss.app._common.enums.ErrorCode;
import com.gmail.llemaxiss.app._common.exception.GlobalExceptionHandler;
import com.gmail.llemaxiss.app._common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app._common.model.response.ErrorResponse;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GlobalExceptionHandlerTest {
  
  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
  
  //region Business and security
  @Nested
  class BusinessAndSecurity {
    
    @Test
    public void commonExceptionIsBadRequest() {
      String message = "message";
      
      CommonException ex = new CommonException(ErrorCode.USER_CONTACT_NOT_FOUND, message);
      
      ResponseEntity<ErrorResponse> result = handler.handleCommonException(ex);
      
      assertResponse(result, HttpStatus.BAD_REQUEST, ErrorCode.USER_CONTACT_NOT_FOUND);
      
      assertThat(
        result.getBody()
          .getMessage()
      )
        .isEqualTo(message);
    }
    
    @Test
    public void authenticationExceptionIsUnauthorized() {
      ResponseEntity<ErrorResponse> result = handler.handleAuthenticationException(
        new BadCredentialsException("bad")
      );
      
      assertResponse(result, HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION_FAILED);
    }
    
    @Test
    public void accessDeniedExceptionIsForbidden() {
      ResponseEntity<ErrorResponse> result = handler.handleAccessDeniedException(
        new AccessDeniedException("denied")
      );
      
      assertResponse(result, HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED);
    }
    
    @Test
    public void unexpectedExceptionIsInternalError() {
      String message = "message";
      
      ResponseEntity<ErrorResponse> result = handler.handleGenericException(
        new IllegalStateException(message)
      );
      
      assertResponse(result, HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR);
      
      assertThat(
        result.getBody()
          .getMessage()
      )
        .doesNotContain(message);
    }
    
  }
  //endregion Business and security
  
  //region Spring MVC
  @Nested
  class SpringMvc {
    
    @Test
    public void missingParameterIsBadRequestWithFieldError() {
      String parameterName = "id";
      
      MissingServletRequestParameterException ex = new MissingServletRequestParameterException(
        parameterName,
        UUID.class.getSimpleName()
      );
      
      ResponseEntity<ErrorResponse> result = handler.handleMissingParameterException(ex);
      
      assertResponse(result, HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED);
      
      assertThat(
        result.getBody()
          .getFieldErrors()
      )
        .singleElement()
        .satisfies(fieldError -> {
          assertThat(
            fieldError.getField()
          )
            .isEqualTo(parameterName);
          
          assertThat(
            fieldError.getCode()
          )
            .isEqualTo("Missing");
        });
    }
    
    @Test
    public void typeMismatchIsBadRequestWithFieldError() {
      String parameterName = "id";
      
      MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
        "abc",
        UUID.class,
        parameterName,
        mock(MethodParameter.class),
        new IllegalArgumentException()
      );
      
      ResponseEntity<ErrorResponse> result = handler.handleTypeMismatchException(ex);
      
      assertResponse(result, HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED);
      
      assertThat(
        result.getBody()
          .getFieldErrors()
      )
        .singleElement()
        .satisfies(fieldError -> {
          assertThat(
            fieldError.getField()
          )
            .isEqualTo(parameterName);
          
          assertThat(
            fieldError.getCode()
          )
            .isEqualTo("TypeMismatch");
        });
    }
    
    @Test
    public void notReadableBodyIsBadRequest() {
      String content = "content";
      
      HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
        "JSON parse error: " + content,
        mock(HttpInputMessage.class)
      );
      
      ResponseEntity<ErrorResponse> result = handler.handleNotReadableException(ex);
      
      assertResponse(result, HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST);
      
      assertThat(
        result.getBody()
          .getMessage()
      )
        .doesNotContain(content);
    }
    
    @Test
    public void methodNotSupportedIsMethodNotAllowed() {
      ResponseEntity<ErrorResponse> result = handler.handleMethodNotSupportedException(
        new HttpRequestMethodNotSupportedException("DELETE")
      );
      
      assertResponse(result, HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.METHOD_NOT_ALLOWED);
    }
    
    @Test
    public void mediaTypeNotSupportedIsUnsupportedMediaType() {
      ResponseEntity<ErrorResponse> result = handler.handleMediaTypeNotSupportedException(
        new HttpMediaTypeNotSupportedException("Content-Type is not supported")
      );
      
      assertResponse(result, HttpStatus.UNSUPPORTED_MEDIA_TYPE, ErrorCode.UNSUPPORTED_MEDIA_TYPE);
    }
    
    @Test
    public void noResourceFoundIsNotFound() {
      ResponseEntity<ErrorResponse> result = handler.handleNoResourceFoundException(
        new NoResourceFoundException(HttpMethod.GET, AppProperty.API_URL_PART + "/unknown")
      );
      
      assertResponse(result, HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND);
    }
    
  }
  //endregion Spring MVC
  
  private void assertResponse(@NotNull ResponseEntity<ErrorResponse> result, @NotNull HttpStatus status,
                              @NotNull ErrorCode errorCode) {
    assertThat(
      result.getStatusCode()
    )
      .isEqualTo(status);
    
    assertThat(
      result.getBody()
    )
      .isNotNull();
    
    assertThat(
      result.getBody()
        .getErrorCode()
    )
      .isEqualTo(errorCode);
  }
  
}