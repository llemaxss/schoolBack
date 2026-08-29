package com.gmail.llemaxiss.app.user.service;

import com.gmail.llemaxiss.app.common.security.util.SecurityUtil;
import com.gmail.llemaxiss.app.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  /**
   * Get {@link User} by his username
   *
   * @return {@link User}
   *
   * @since v1.0.0
   */
  @NotNull
  User getUserByUsername(@NotNull String username) throws EntityNotFoundException;

  /**
   * Get current logged in {@link User}
   *
   * @return logged in {@link User}
   *
   * @see SecurityUtil#getCurrentUsername()
   * @since v1.0.0
   */
  @NotNull
  User getCurrentUser() throws EntityNotFoundException;

  /**
   * Get {@link UserDetails} of current logged in {@link User}
   *
   * @return {@link UserDetails} of logged in {@link User}
   *
   * @see #getCurrentUser()
   * @see AppUserDetails#build(User)
   * @since v1.0.0
   */
  @NotNull
  UserDetails getCurrentUserDetails() throws EntityNotFoundException;

  /**
   * Get {@link User} by his id
   *
   * @param id of {@link User} to be found
   *
   * @return {@link User}
   *
   * @since v1.0.0
   */
  @NotNull
  User getUserById(@NotNull UUID id);
}
