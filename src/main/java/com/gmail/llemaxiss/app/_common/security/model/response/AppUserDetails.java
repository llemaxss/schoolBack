package com.gmail.llemaxiss.app._common.security.model.response;

import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.util.UserUtil;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
public class AppUserDetails implements UserDetails {
  
  private final UUID id;
  
  private final String username;
  
  private final String password;
  
  private final Boolean isActive;
  
  private final Collection<? extends GrantedAuthority> authorities;

  public AppUserDetails(@NotNull User user) {
    this.id = user.getId();

    this.username = user.getUsername();
    this.password = user.getPassword();
    this.isActive = user.getIsActive();

    this.authorities = UserUtil.getAuthorities(user);
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isActive;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return isActive;
  }
  
}
