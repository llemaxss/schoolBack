package com.gmail.llemaxiss.app.userContact.service;

import com.gmail.llemaxiss.app.userContact.entity.UserContact;
import com.gmail.llemaxiss.app.userContact.model.request.UserContactCreateModel;
import com.gmail.llemaxiss.app.userContact.model.request.UserContactUpdateModel;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface UserContactService {
  
  /**
   * Get {@link UserContact} by id
   *
   * @param id of {@link UserContact} to be found
   *
   * @return {@link UserContact}
   */
  @NotNull
  UserContact getUserContactById(@NotNull UUID id);
  
  /**
   * Get all {@link UserContact} of the user profile
   *
   * @param userProfileId id of the user profile
   *
   * @return list of {@link UserContact}
   */
  @NotNull
  List<UserContact> getUserContactsByUserProfileId(@NotNull UUID userProfileId);
  
  /**
   * Creates a new {@link UserContact}
   *
   * <p>
   * If the new contact is primary, the previous primary contact of the same type is demoted.
   * </p>
   *
   * @param model the creation request model
   *
   * @return the created {@link UserContact} entity
   */
  @NotNull
  UserContact createUserContact(@NotNull UserContactCreateModel model);
  
  /**
   * Updates an existing {@link UserContact}
   *
   * @param id    the user contact id
   * @param model the update request model
   *
   * @return the updated {@link UserContact} entity
   */
  @NotNull
  UserContact updateUserContact(@NotNull UUID id, @NotNull UserContactUpdateModel model);
  
  /**
   * Deletes a {@link UserContact} by its id.
   *
   * @param id the user contact id
   */
  void deleteUserContact(@NotNull UUID id);
  
}