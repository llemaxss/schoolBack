package com.gmail.llemaxiss.app.common.security.model;

import com.gmail.llemaxiss.app.role.service.RoleService;
import com.gmail.llemaxiss.app.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class AppUserDetails implements UserDetails {

  private final UUID id;

  private final String username;

  private final String password;
  
  private final Boolean isActive;

  private final Collection<? extends GrantedAuthority> authorities;

  public AppUserDetails(@NotNull UUID id,
                        @NotNull String username, @NotNull String password,
                        @NotNull Boolean isActive,
                        @NotNull Set<String> roles) {
    this.id = id;

    this.username = username;
    this.password = password;
    this.isActive = isActive;

    this.authorities = roles.stream()
      .map(role ->
        new SimpleGrantedAuthority(RoleService.ROLE_PREFIX + role)
      )
      .collect(Collectors.toList());
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

  @NotNull
  public static AppUserDetails build(@NotNull User user) {
    Set<String> roles = user.getUserRoles()
      .stream()
      .map(ur ->
        ur.getRole()
          .getType()
          .getId()
      )
      .collect(Collectors.toSet());

    return new AppUserDetails(
      user.getId(),
      user.getUsername(),
      user.getPassword(),
      user.getIsActive(),
      roles
    );
  }
}
