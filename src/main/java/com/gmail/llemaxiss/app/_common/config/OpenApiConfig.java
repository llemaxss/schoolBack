package com.gmail.llemaxiss.app._common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
  info = @Info(
    title = "School Management API",
    description = "REST API for school management system"
  )
)
@SecurityScheme(
  name = OpenApiConfig.BEARER_AUTH,
  type = SecuritySchemeType.HTTP,
  scheme = "bearer",
  bearerFormat = "JWT",
  description = "JWT authorization header. Example: 'Bearer {token}'"
)
public class OpenApiConfig {

  public static final String BEARER_AUTH = "bearerAuth";

  public static final String NO_AUTH = "";
}
