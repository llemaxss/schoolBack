package com.gmail.llemaxiss.app.auth.controller;

import com.gmail.llemaxiss.app._common.config.OpenApiConfig;
import com.gmail.llemaxiss.app._common.controller.CommonController;
import com.gmail.llemaxiss.app._common.security.model.request.LoginRequest;
import com.gmail.llemaxiss.app._common.security.model.response.LoginResponse;
import com.gmail.llemaxiss.app.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gmail.llemaxiss.app._common.component.AppProperty.API_URL_PART;

@RequiredArgsConstructor
@RestController
@RequestMapping(AuthController.URL)
@Tag(
  name = "Auth",
  description = "Authentication and user session management"
)
@SecurityRequirement(name = OpenApiConfig.NO_AUTH)
public class AuthController extends CommonController {
  
  public static final String URL = API_URL_PART + "/auth";
  
  private final AuthService authService;

  @PostMapping("/login")
  @Operation(
    summary = "Log in",
    description = "Accepts login and password, returns JWT token and user information"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Successful login",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = LoginResponse.class)
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Incorrect login or password",
      content = @Content()
    )
  })
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    LoginResponse loginResponse = authService.login(loginRequest);

    return ResponseEntity.ok(loginResponse);
  }
  
}
