package com.gmail.llemaxiss.app.common.property.component;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppProperty {

  public static final String API_URL_PART = "/api/v1";

  public static final String TABLE_PREFIX = "school_";

  @Value("${app.jwt-secret:ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890}")
  private String jwtSecret;

  @Value("${app.jwt-expiration-millis:86400000}") // 24h
  private long jwtExpirationMs;
}
