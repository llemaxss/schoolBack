package com.gmail.llemaxiss.app.role.service;

import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public interface RoleService {

  String ROLE_PREFIX = "ROLE_";

  /**
   * Get {@link Role} by id
   *
   * @param id of {@link Role} to be found
   *
   * @return {@link Role}
   *
   */
  @NotNull
  Role getRoleById(@NotNull UUID id) throws EntityNotFoundException;

  /**
   * Get set of {@link Role} by {@link RoleType}
   *
   * @param roleType for getting set of {@link Role}
   *
   * @return set of {@link Role}
   *
   */
  @NotNull
  Set<Role> getRolesByType(@NotNull RoleType roleType);
  
}
