package com.gmail.llemaxiss.app.common.security.component.jwt;

import com.gmail.llemaxiss.app.common.property.component.AppProperty;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtHelper.class);

  private final AppProperty appProperty;

  public JwtHelper(AppProperty appProperty) {
    this.appProperty = appProperty;
  }

  @NotNull
  private Key key() {
    String jwtSecret = appProperty.getJwtSecret();

    byte[] jwtSecretBytes = jwtSecret.getBytes();

    return Keys.hmacShaKeyFor(jwtSecretBytes);
  }

  @NotNull
  public String generateJwtToken(@NotNull String username) {
    Key key = key();

    Date issuedAt = new Date();
    Date expiration = new Date(
      System.currentTimeMillis()
        + appProperty.getJwtExpirationMs()
    );

    return Jwts.builder()
      .setSubject(username)
      .setIssuedAt(issuedAt)
      .setExpiration(expiration)
      .signWith(key)
      .compact();
  }

  @NotNull
  public String getUsernameFromJwtToken(@NotNull String token) {
    Key key = key();

    return Jwts.parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }

  public boolean validateJwtToken(@NotNull String authToken) {
    try {
      Key key = key();

      Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(authToken);

      return true;
    } catch (SecurityException e) {
      LOGGER.debug("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      LOGGER.debug("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      LOGGER.debug("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      LOGGER.debug("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      LOGGER.debug("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
  
}
