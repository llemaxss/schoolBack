package com.gmail.llemaxiss.app.unit._common;

import com.gmail.llemaxiss.app._common.component.AppAuditorAware;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AppAuditorAwareTest {
  
  private final AppAuditorAware appAuditorAware = new AppAuditorAware();
  
  @AfterEach
  public void tearDown() {
    SecurityContextHolder.clearContext();
  }
  
  @Test
  public void returnsUsernameWhenAuthenticated() {
    String username = RoleType.TEACHER.getId()
      .toLowerCase();
    
    SecurityContextHolder.getContext()
      .setAuthentication(
        new UsernamePasswordAuthenticationToken(
          username,
          null,
          List.of()
        )
      );
    
    assertThat(
      appAuditorAware.getCurrentAuditor()
    )
      .contains(username);
  }
  
  @Test
  public void returnsSystemWhenNoAuthentication() {
    assertThat(
      appAuditorAware.getCurrentAuditor()
    )
      .contains(AppAuditorAware.DEFAULT_AUDITOR);
  }
  
  @Test
  public void returnsSystemWhenNotAuthenticated() {
    SecurityContextHolder.getContext()
      .setAuthentication(
        new UsernamePasswordAuthenticationToken(
          "username",
          "password"
        )
      );
    
    assertThat(
      appAuditorAware.getCurrentAuditor()
    )
      .contains(AppAuditorAware.DEFAULT_AUDITOR);
  }
  
  @Test
  public void returnsSystemWhenAnonymous() {
    SecurityContextHolder.getContext()
      .setAuthentication(
        new AnonymousAuthenticationToken(
          "key",
          "anonymousUser",
          List.of(
            new SimpleGrantedAuthority("ROLE_ANONYMOUS")
          )
        )
      );
    
    assertThat(
      appAuditorAware.getCurrentAuditor()
    )
      .contains(AppAuditorAware.DEFAULT_AUDITOR);
  }
  
}
