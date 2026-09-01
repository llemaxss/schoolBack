package com.gmail.llemaxiss.app.userRole.service;

import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import com.gmail.llemaxiss.app.role.service.RoleService;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.service.UserService;
import com.gmail.llemaxiss.app.userRole.entity.UserRole;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Service
public class UserRoleServiceImpl implements UserRoleService {

  private final UserService userService;

  private final RoleService roleService;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRoleType(@NotNull RoleType roleType) {
    User user = userService.getCurrentUser();

    return hasRoleType(user, roleType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRoleType(@NotNull UUID userId, @NotNull RoleType roleType) {
    User user = userService.getUserById(userId);

    return hasAllRoleTypes(user, Collections.singleton(roleType));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRoleType(@NotNull User user, @NotNull RoleType roleType) {
    return hasAllRoleTypes(user, Collections.singleton(roleType));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAnyRoleType(@NotNull Collection<RoleType> roleTypes) {
    User user = userService.getCurrentUser();

    return hasAnyRoleType(user, roleTypes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAnyRoleType(@NotNull UUID userId, @NotNull Collection<RoleType> roleTypes) {
    User user = userService.getUserById(userId);

    return hasAnyRoleType(user, roleTypes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAnyRoleType(@NotNull User user, @NotNull Collection<RoleType> roleTypes) {
    Set<RoleType> userRoleTypes = getUserRoleTypes(user);

    return userRoleTypes.stream()
      .anyMatch(roleTypes::contains);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAllRoleTypes(@NotNull Collection<RoleType> roleTypes) {
    User user = userService.getCurrentUser();

    return hasAllRoleTypes(user, roleTypes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAllRoleTypes(@NotNull UUID userId, @NotNull Collection<RoleType> roleTypes) {
    User user = userService.getUserById(userId);

    return hasAllRoleTypes(user, roleTypes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAllRoleTypes(@NotNull User user, @NotNull Collection<RoleType> roleTypes) {
    Set<RoleType> userRoleTypes = getUserRoleTypes(user);

    return roleTypes.containsAll(userRoleTypes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Set<RoleType> getUserRoleTypes(@NotNull UUID userId) {
    User user = userService.getUserById(userId);

    return getUserRoleTypes(user);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Set<RoleType> getUserRoleTypes(@NotNull User user) {
    Set<UserRole> userRoles = user.getUserRoles();

    if (CollectionUtils.isEmpty(userRoles)) {
      return new HashSet<>();
    }

    return userRoles.stream()
      .map(UserRole::getRole)
      .map(Role::getType)
      .collect(Collectors.toSet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRole(@NotNull UUID roleId) {
    return hasAllRoleIds(Collections.singleton(roleId));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRole(@NotNull UUID userId, @NotNull UUID roleId) {
    return hasAllRoleIds(userId, Collections.singleton(roleId));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRole(@NotNull User user, @NotNull UUID roleId) {
    return hasAllRoleIds(user, Collections.singleton(roleId));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRole(@NotNull Role role) {
    return hasAllRoles(Collections.singleton(role));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRole(@NotNull UUID userId, @NotNull Role role) {
    return hasAllRoles(userId, Collections.singleton(role));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRole(@NotNull User user, @NotNull Role role) {
    return hasAllRoles(user, Collections.singleton(role));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAnyRoleIds(@NotNull Collection<UUID> roleIds) {
    User user = userService.getCurrentUser();

    return hasAnyRoleIds(user, roleIds);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAnyRoleIds(@NotNull UUID userId, @NotNull Collection<UUID> roleIds) {
    User user = userService.getUserById(userId);

    return hasAnyRoleIds(user, roleIds);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAnyRoleIds(@NotNull User user, @NotNull Collection<UUID> roleIds) {
    if (CollectionUtils.isEmpty(user.getUserRoles())) {
      return false;
    }

    return user.getUserRoles()
      .stream()
      .map(UserRole::getRole)
      .map(Role::getId)
      .anyMatch(roleIds::contains);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAnyRole(@NotNull Collection<Role> roles) {
    User user = userService.getCurrentUser();

    return hasAnyRole(user, roles);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAnyRole(@NotNull UUID userId, @NotNull Collection<Role> roles) {
    User user = userService.getUserById(userId);

    return hasAnyRole(user, roles);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAnyRole(@NotNull User user, @NotNull Collection<Role> roles) {
    if (CollectionUtils.isEmpty(user.getUserRoles())) {
      return false;
    }

    return user.getUserRoles()
      .stream()
      .map(UserRole::getRole)
      .anyMatch(roles::contains);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAllRoleIds(@NotNull Collection<UUID> roleIds) {
    User user = userService.getCurrentUser();

    return hasAllRoleIds(user, roleIds);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAllRoleIds(@NotNull UUID userId, @NotNull Collection<UUID> roleIds) {
    User user = userService.getUserById(userId);

    return hasAllRoleIds(user, roleIds);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAllRoleIds(@NotNull User user, @NotNull Collection<UUID> roleIds) {
    if (CollectionUtils.isEmpty(user.getUserRoles())) {
      return false;
    }

    return user.getUserRoles()
      .stream()
      .map(UserRole::getRole)
      .map(Role::getId)
      .allMatch(roleIds::contains);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAllRoles(@NotNull Collection<Role> roles) {
    User user = userService.getCurrentUser();

    return hasAllRoles(user, roles);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAllRoles(@NotNull UUID userId, @NotNull Collection<Role> roles) {
    User user = userService.getUserById(userId);

    return hasAllRoles(user, roles);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAllRoles(@NotNull User user, @NotNull Collection<Role> roles) {
    if (CollectionUtils.isEmpty(user.getUserRoles())) {
      return false;
    }

    return user.getUserRoles()
      .stream()
      .map(UserRole::getRole)
      .allMatch(roles::contains);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Set<Role> getUserRoles(@NotNull UUID userId) {
    User user = userService.getUserById(userId);

    return user.getUserRoles()
      .stream()
      .map(UserRole::getRole)
      .collect(Collectors.toSet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Set<User> getUsersByRoleType(@NotNull RoleType roleType) {
    Set<User> users = new HashSet<>();

    roleService.getRolesByType(roleType)
      .forEach(role -> {
        Set<User> usersByRole = getUsersByRole(role);

        users.addAll(usersByRole);
      });

    return users;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Set<User> getUsersByRole(@NotNull UUID roleId) {
    Role role = roleService.getRoleById(roleId);

    return getUsersByRole(role);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Set<User> getUsersByRole(@NotNull Role role) {
    if (CollectionUtils.isEmpty(role.getUserRoles())) {
      return new HashSet<>();
    }

    return role.getUserRoles()
      .stream()
      .map(UserRole::getUser)
      .collect(Collectors.toSet());
  }
  
}
