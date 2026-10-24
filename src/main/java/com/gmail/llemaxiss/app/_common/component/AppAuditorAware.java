package com.gmail.llemaxiss.app._common.component;

import com.gmail.llemaxiss.app.role.enums.RoleType;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Provides the current auditor name for {@code createdBy} / {@code updatedBy} fields
 *
 * <p>
 * If there is no authenticated user (e.g. background jobs), {@link RoleType#SYSTEM} is used.
 * </p>
 */
@Component
public class AppAuditorAware implements AuditorAware<String> {
  
  public static final String DEFAULT_AUDITOR = RoleType.SYSTEM.getId().toLowerCase();
  
  @Override
  @NonNull
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext()
      .getAuthentication();
    
    boolean isNotAuthenticated = authentication == null
      || !authentication.isAuthenticated()
      || authentication instanceof AnonymousAuthenticationToken;
    
    if (isNotAuthenticated) {
      return Optional.of(DEFAULT_AUDITOR);
    }
    
    return Optional.of(
      authentication.getName()
    );
  }
  
}
