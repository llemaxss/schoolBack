package com.gmail.llemaxiss.app.common.security.model.response;

import com.gmail.llemaxiss.app.common.property.component.AppProperty;
import com.gmail.llemaxiss.app.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Schema(description = "Application realisation of spring UserDetails")
public class AppUserDetails implements UserDetails {
  
  @NotNull
  @Schema(
    description = "User id",
    example = "10000000-.."
  )
  private final UUID id;
  
  @NotEmpty
  @Schema(
    description = "User login",
    example = "admin"
  )
  private final String username;
  
  @NotEmpty
  @Schema(
    description = "User password",
    example = "$2a$..."
  )
  private final String password;
  
  @NotNull
  @Schema(
    description = "User activity status",
    example = "true"
  )
  private final Boolean isActive;

  private final Collection<? extends GrantedAuthority> authorities;

  public AppUserDetails(@NotNull User user) {
    this.id = user.getId();

    this.username = user.getUsername();
    this.password = user.getPassword();
    this.isActive = user.getIsActive();

    this.authorities = extractAuthorities(user);
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
  private Collection<? extends GrantedAuthority> extractAuthorities(@NotNull User user) {
    return user.getUserRoles()
      .stream()
      .map(userRole ->
        userRole.getRole()
          .getType()
      )
      .flatMap(role -> {
        Stream<GrantedAuthority> roleAuth = Stream.of(
          new SimpleGrantedAuthority(AppProperty.SPRING_ROLE_PREFIX + role.getId())
        );

        Stream<GrantedAuthority> permAuth = role.getPermissions()
          .stream()
          .map(perm ->
            new SimpleGrantedAuthority(perm.getId())
          );

        return Stream.concat(roleAuth, permAuth);
      })
      .collect(Collectors.toList());
  }
}
