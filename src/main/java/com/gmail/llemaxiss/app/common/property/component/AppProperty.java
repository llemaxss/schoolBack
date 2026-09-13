package com.gmail.llemaxiss.app.common.property.component;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Common component of application properties
 */
@Getter
@Component
public class AppProperty {
  
  public static final int API_VERSION = 1;
  
  public static final String API_URL_PART = "/api/v" + API_VERSION;

  public static final String TABLE_PREFIX = "school_";
  
  public static final String SPRING_ROLE_PREFIX = "ROLE_";

  @Value("${app.jwt-secret:ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890}")
  private String jwtSecret;

  @Value("${app.jwt-expiration-millis:0}")
  private long jwtExpirationMs;
  
  @Value("${app.version:unknown}")
  private String appVersion;
}
