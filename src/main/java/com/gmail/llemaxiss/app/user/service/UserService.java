package com.gmail.llemaxiss.app.user.service;

import com.gmail.llemaxiss.app.common.security.model.AppUserDetails;
import com.gmail.llemaxiss.app.common.security.util.SecurityUtil;
import com.gmail.llemaxiss.app.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  /**
   * Get {@link User} by his username
   *
   * @return {@link User}
   *
   */
  @NotNull
  User getUserByUsername(@NotNull String username) throws EntityNotFoundException;

  /**
   * Get current logged in {@link User}
   *
   * @return logged in {@link User}
   *
   * @see SecurityUtil#getCurrentUsername()
   */
  @NotNull
  User getCurrentUser() throws EntityNotFoundException;

  /**
   * Get {@link AppUserDetails} of current logged in {@link User}
   *
   * @return {@link AppUserDetails} of logged in {@link User}
   *
   * @see #getCurrentUser()
   *
   */
  @NotNull
  AppUserDetails getCurrentUserDetails() throws EntityNotFoundException;

  /**
   * Get {@link User} by his id
   *
   * @param id of {@link User} to be found
   *
   * @return {@link User}
   *
   */
  @NotNull
  User getUserById(@NotNull UUID id);
  
}
