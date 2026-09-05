package com.gmail.llemaxiss.app.userRole.service;

import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import com.gmail.llemaxiss.app.user.entity.User;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface UserRoleService {

  /**
   * Check current {@link User} has {@link RoleType}
   *
   * @param roleType is {@link RoleType} to check
   *
   * @return true, if current {@link User} has {@link RoleType}, else return false
   *
   * @see #hasRoleType(User, RoleType)
   */
  boolean hasRoleType(@NotNull RoleType roleType);

  /**
   * Check {@link User} by id has {@link RoleType}
   *
   * @param userId   is {@link User} id to check
   * @param roleType is {@link RoleType} to check
   *
   * @return true, if {@link User} by id has {@link RoleType}, else return false
   *
   * @see #hasRoleType(User, RoleType)
   */
  boolean hasRoleType(@NotNull UUID userId, @NotNull RoleType roleType);

  /**
   * Check {@link User} has {@link RoleType}
   *
   * @param user     is {@link User} to check
   * @param roleType is {@link RoleType} to check
   *
   * @return true, if {@link User} has {@link RoleType}, else return false
   *
   * @see #hasAllRoleTypes(User, Collection)
   */
  boolean hasRoleType(@NotNull User user, @NotNull RoleType roleType);

  /**
   * Check current {@link User} has any {@link RoleType}
   *
   * @param roleTypes is collection of {@link RoleType} to check
   *
   * @return true, if current {@link User} has any {@link RoleType}, else return false
   *
   * @see #hasAnyRoleType(User, Collection)
   */
  boolean hasAnyRoleType(@NotNull Collection<RoleType> roleTypes);

  /**
   * Check {@link User} by id has any {@link RoleType}
   *
   * @param userId    is {@link User} id to check
   * @param roleTypes is collection of {@link RoleType} to check
   *
   * @return true, if {@link User} by id has any {@link RoleType}, else return false
   *
   * @see #hasAnyRoleType(User, Collection)
   */
  boolean hasAnyRoleType(@NotNull UUID userId, @NotNull Collection<RoleType> roleTypes);

  /**
   * Check {@link User} id has any {@link RoleType}
   *
   * @param user      is {@link User} to check
   * @param roleTypes is collection of {@link RoleType} to check
   *
   * @return true, if {@link User} has any {@link RoleType}, else return false
   */
  boolean hasAnyRoleType(@NotNull User user, @NotNull Collection<RoleType> roleTypes);

  /**
   * Check current {@link User} has all {@link RoleType}
   *
   * @param roleTypes is collection of {@link RoleType} to check
   *
   * @return true, if current {@link User} has all {@link RoleType}, else return false
   *
   * @see #hasAllRoleTypes(User, Collection)
   */
  boolean hasAllRoleTypes(@NotNull Collection<RoleType> roleTypes);

  /**
   * Check {@link User} by id has all {@link RoleType}
   *
   * @param userId    is {@link User} id to check
   * @param roleTypes is collection of {@link RoleType} to check
   *
   * @return true, if {@link User} by id has all {@link RoleType}, else return false
   *
   * @see #hasAllRoleTypes(User, Collection)
   */
  boolean hasAllRoleTypes(@NotNull UUID userId, @NotNull Collection<RoleType> roleTypes);

  /**
   * Check {@link User} has all {@link RoleType}
   *
   * @param user      is {@link User} to check
   * @param roleTypes is collection of {@link RoleType} to check
   *
   * @return true, if {@link User} has all {@link RoleType}, else return false
   */
  boolean hasAllRoleTypes(@NotNull User user, @NotNull Collection<RoleType> roleTypes);

  /**
   * Get set of {@link RoleType} of sent {@link User} id
   *
   * @param userId is {@link User} id to check
   *
   * @return set of {@link RoleType}
   *
   * @see #getUserRoleTypes(User)
   */
  @NotNull
  Set<RoleType> getUserRoleTypes(@NotNull UUID userId);

  /**
   * Get set of {@link RoleType} of sent {@link User}
   *
   * @param user is {@link User}
   *
   * @return set of {@link RoleType}
   */
  @NotNull
  Set<RoleType> getUserRoleTypes(@NotNull User user);

  /**
   * Check current {@link User} has {@link Role} by id
   *
   * @param roleId is {@link Role} id to check
   *
   * @return true, if current {@link User} has {@link Role} by id, else return false
   *
   * @see #hasRole(User, UUID)
   */
  boolean hasRole(@NotNull UUID roleId);

  /**
   * Check {@link User} by id has {@link Role} by id
   *
   * @param userId is {@link User} id to check
   * @param roleId is {@link Role} id to check
   *
   * @return true, if {@link User} by id has {@link Role} by id, else return false
   *
   * @see #hasRole(User, UUID)
   */
  boolean hasRole(@NotNull UUID userId, @NotNull UUID roleId);

  /**
   * Check {@link User} has {@link Role} by id
   *
   * @param user   is {@link User} to check
   * @param roleId is {@link Role} id to check
   *
   * @return true, if {@link User} has {@link Role} by id, else return false
   *
   * @see #hasAllRoleIds(User, Collection)
   */
  boolean hasRole(@NotNull User user, @NotNull UUID roleId);

  /**
   * Check current {@link User} has {@link Role}
   *
   * @param role is {@link Role} to check
   *
   * @return true, if current {@link User} has {@link Role}, else return false
   *
   * @see #hasRole(User, Role)
   */
  boolean hasRole(@NotNull Role role);

  /**
   * Check {@link User} by id has {@link Role}
   *
   * @param userId is {@link User} id to check
   * @param role   is {@link Role} to check
   *
   * @return true, if {@link User} by id has {@link Role}, else return false
   *
   * @see #hasRole(User, Role)
   */
  boolean hasRole(@NotNull UUID userId, @NotNull Role role);

  /**
   * Check {@link User} has {@link Role}
   *
   * @param user is {@link User} to check
   * @param role is {@link Role} to check
   *
   * @return true, if {@link User} has {@link Role}, else return false
   *
   * @see #hasAllRoles(User, Collection)
   */
  boolean hasRole(@NotNull User user, @NotNull Role role);

  /**
   * Check current {@link User} has any {@link Role} by ids
   *
   * @param roleIds is collection of {@link Role} ids to check
   *
   * @return true, if current {@link User} has any {@link Role} by ids, else return false
   *
   * @see #hasAnyRoleIds(User, Collection)
   */
  boolean hasAnyRoleIds(@NotNull Collection<UUID> roleIds);

  /**
   * Check {@link User} by id has any {@link Role} by ids
   *
   * @param userId  is {@link User} id to check
   * @param roleIds is collection of {@link Role} ids to check
   *
   * @return true, if {@link User} by id has any {@link Role} by ids, else return false
   *
   * @see #hasAnyRoleIds(User, Collection)
   */
  boolean hasAnyRoleIds(@NotNull UUID userId, @NotNull Collection<UUID> roleIds);

  /**
   * Check {@link User} has any {@link Role} by ids
   *
   * @param user    is {@link User} to check
   * @param roleIds is collection of {@link Role} ids to check
   *
   * @return true, if {@link User} has any {@link Role} by ids, else return false
   *
   * @see #hasAnyRoleIds(User, Collection)
   */
  boolean hasAnyRoleIds(@NotNull User user, @NotNull Collection<UUID> roleIds);

  /**
   * Check current {@link User} has any {@link Role}
   *
   * @param roles is collection of {@link Role} to check
   *
   * @return true, if current {@link User} has any {@link Role}, else return false
   *
   * @see #hasAnyRole(User, Collection)
   */
  boolean hasAnyRole(@NotNull Collection<Role> roles);

  /**
   * Check {@link User} by id has any {@link Role}
   *
   * @param userId is {@link User} id to check
   * @param roles  is collection of {@link Role} to check
   *
   * @return true, if {@link User} by id has any {@link Role}, else return false
   *
   * @see #hasAnyRole(User, Collection)
   */
  boolean hasAnyRole(@NotNull UUID userId, @NotNull Collection<Role> roles);

  /**
   * Check {@link User} has any {@link Role}
   *
   * @param user  is {@link User} to check
   * @param roles is collection of {@link Role} to check
   *
   * @return true, if {@link User} has any {@link Role}, else return false
   */
  boolean hasAnyRole(@NotNull User user, @NotNull Collection<Role> roles);

  /**
   * Check current {@link User} has all {@link Role} by ids
   *
   * @param roleIds is collection of {@link Role} ids to check
   *
   * @return true, if current {@link User} has all {@link Role} by ids, else return false
   *
   * @see #hasAllRoleIds(User, Collection)
   */
  boolean hasAllRoleIds(@NotNull Collection<UUID> roleIds);

  /**
   * Check {@link User} by id has all {@link Role} by ids
   *
   * @param userId  is {@link User} id to check
   * @param roleIds is collection of {@link Role} ids to check
   *
   * @return true, if {@link User} by id has all {@link Role} by ids, else return false
   *
   * @see #hasAnyRoleIds(User, Collection)
   */
  boolean hasAllRoleIds(@NotNull UUID userId, @NotNull Collection<UUID> roleIds);

  /**
   * Check {@link User} has all {@link Role} by ids
   *
   * @param user    is {@link User} to check
   * @param roleIds is collection of {@link Role} ids to check
   *
   * @return true, if {@link User} has all {@link Role} by ids, else return false
   */
  boolean hasAllRoleIds(@NotNull User user, @NotNull Collection<UUID> roleIds);

  /**
   * Check current {@link User} has all {@link Role}
   *
   * @param roles is collection of {@link Role} to check
   *
   * @return true, if current {@link User} has all {@link Role}, else return false
   *
   * @see #hasAllRoles(User, Collection)
   */
  boolean hasAllRoles(@NotNull Collection<Role> roles);

  /**
   * Check {@link User} by id has all {@link Role}
   *
   * @param userId is {@link User} id to check
   * @param roles  is collection of {@link Role} to check
   *
   * @return true, if {@link User} by id has all {@link Role}, else return false
   *
   * @see #hasAllRoles(User, Collection)
   */
  boolean hasAllRoles(@NotNull UUID userId, @NotNull Collection<Role> roles);

  /**
   * Check {@link User} has all {@link Role}
   *
   * @param user  is {@link User} to check
   * @param roles is collection of {@link Role} to check
   *
   * @return true, if {@link User} has all {@link Role}, else return false
   */
  boolean hasAllRoles(@NotNull User user, @NotNull Collection<Role> roles);

  /**
   * Get set of {@link Role} of sent {@link User} id
   *
   * @param userId is {@link User} id to check
   *
   * @return set of {@link Role}
   */
  @NotNull
  Set<Role> getUserRoles(@NotNull UUID userId);

  /**
   * Get set of {@link User} by sent {@link RoleType}
   *
   * @param roleType is {@link RoleType} for getting set of {@link User}
   *
   * @return set of {@link User}
   */
  @NotNull
  Set<User> getUsersByRoleType(@NotNull RoleType roleType);

  /**
   * Get set of {@link User} by sent {@link Role} id
   *
   * @param roleId is {@link Role} id for getting set of {@link User}
   *
   * @return set of {@link User}
   *
   * @see #getUsersByRole(Role)
   */
  @NotNull
  Set<User> getUsersByRole(@NotNull UUID roleId);

  /**
   * Get set of {@link User} by sent {@link Role}
   *
   * @param role is {@link Role} for getting set of {@link User}
   *
   * @return set of {@link User}
   */
  @NotNull
  Set<User> getUsersByRole(@NotNull Role role);
  
}
