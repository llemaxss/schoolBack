package com.gmail.llemaxiss.app.userProfile.service;

import com.gmail.llemaxiss.app.userProfile.entity.UserProfile;
import com.gmail.llemaxiss.app.userProfile.model.request.UserProfileCreateModel;
import com.gmail.llemaxiss.app.userProfile.model.request.UserProfileUpdateModel;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface UserProfileService {
  
  /**
   * Get {@link UserProfile} by id
   *
   * @param id of {@link UserProfile} to be found
   *
   * @return {@link UserProfile}
   */
  @NotNull
  UserProfile getUserProfileById(@NotNull UUID id);
  
  /**
   * Creates a new {@link UserProfile}
   *
   * @param model the creation request model
   *
   * @return the created {@link UserProfile} entity
   */
  @NotNull
  UserProfile createUserProfile(@NotNull UserProfileCreateModel model);
  
  /**
   * Updates an existing {@link UserProfile}
   *
   * @param id    the role id
   * @param model the update request model
   *
   * @return the updated {@link UserProfile} entity
   */
  @NotNull
  UserProfile updateUserProfile(@NotNull UUID id, @NotNull UserProfileUpdateModel model);
  
  /**
   * Deletes a {@link UserProfile} by its id.
   *
   * @param id the role id
   */
  void deleteUserProfile(@NotNull UUID id);
  
}
