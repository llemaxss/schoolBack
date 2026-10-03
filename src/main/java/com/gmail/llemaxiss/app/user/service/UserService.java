package com.gmail.llemaxiss.app.user.service;

import com.gmail.llemaxiss.app.user.entity.User;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  /**
   * Get {@link User} by his username
   *
   * @return {@link User}
   */
  @NotNull
  User getUserByUsername(@NotNull String username);

  /**
   * Get current logged in {@link User}
   *
   * @return logged in {@link User}
   *
   * @see CommonUtil#getCurrentUsername()
   */
  @NotNull
  User getCurrentUser();

  /**
   * Get {@link User} by his id
   *
   * @param id of {@link User} to be found
   *
   * @return {@link User}
   */
  @NotNull
  User getUserById(@NotNull UUID id);
  
}
