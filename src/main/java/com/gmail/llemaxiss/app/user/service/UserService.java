package com.gmail.llemaxiss.app.user.service;

import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.model.request.UserCreateModel;
import com.gmail.llemaxiss.app.user.model.request.UserUpdateModel;
import com.gmail.llemaxiss.app.user.util.UserUtil;
import jakarta.validation.constraints.NotBlank;
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
  User getUserByUsername(@NotBlank String username);

  /**
   * Get current logged in {@link User}
   *
   * @return logged in {@link User}
   *
   * @see UserUtil#getAppUserDetails()
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
  
  /**
   * Get {@link User} by his id, including inactive users
   *
   * <p>
   * For user management only. Use {@link #getUserById(UUID)} for everything else:
   * inactive users must stay hidden there.
   * </p>
   *
   * @param id of {@link User} to be found
   *
   * @return {@link User}
   */
  @NotNull
  User getUserByIdIncludingInactive(@NotNull UUID id);
  
  /**
   * Creates a new active {@link User} with the given temporary password
   *
   * @param model the creation request model
   *
   * @return the created {@link User} entity
   */
  @NotNull
  User createUser(@NotNull UserCreateModel model);
  
  /**
   * Updates an existing {@link User}
   *
   * @param id    the user id
   * @param model the update request model
   *
   * @return the updated {@link User} entity
   */
  @NotNull
  User updateUser(@NotNull UUID id, @NotNull UserUpdateModel model);
  
  /**
   * Deletes a {@link User} by its id.
   *
   * @param id the user id
   */
  void deleteUser(@NotNull UUID id);
  
  /**
   * Activates a {@link User}
   *
   * @param id the user id
   *
   * @return the {@link User} entity
   */
  @NotNull
  User activateUser(@NotNull UUID id);
  
  /**
   * Deactivates a {@link User}
   *
   * @param id the user id
   *
   * @return the {@link User} entity
   */
  @NotNull
  User deactivateUser(@NotNull UUID id);
  
}
