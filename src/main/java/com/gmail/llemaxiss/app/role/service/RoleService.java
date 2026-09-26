package com.gmail.llemaxiss.app.role.service;

import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import com.gmail.llemaxiss.app.role.model.request.RoleCreateModel;
import com.gmail.llemaxiss.app.role.model.request.RoleUpdateModel;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public interface RoleService {

  /**
   * Get {@link Role} by id
   *
   * @param id of {@link Role} to be found
   *
   * @return {@link Role}
   */
  @NotNull
  Role getRoleById(@NotNull UUID id);

  /**
   * Get set of {@link Role} by {@link RoleType}
   *
   * @param roleType for getting set of {@link Role}
   *
   * @return set of {@link Role}
   */
  @NotNull
  Set<Role> getRolesByType(@NotNull RoleType roleType);
  
  /**
   * Creates a new {@link Role}
   *
   * @param model the creation request model
   *
   * @return the created {@link Role} entity
   */
  @NotNull
  Role createRole(@NotNull RoleCreateModel model);
  
  /**
   * Updates an existing {@link Role}
   *
   * @param id    the role id
   * @param model the update request model
   *
   * @return the updated {@link Role} entity
   */
  @NotNull
  Role updateRole(@NotNull UUID id, @NotNull RoleUpdateModel model);
  
  /**
   * Deletes a {@link Role} by its id.
   *
   * @param id the role id
   */
  void deleteRole(@NotNull UUID id);
  
}
