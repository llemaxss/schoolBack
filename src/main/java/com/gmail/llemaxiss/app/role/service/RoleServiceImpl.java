package com.gmail.llemaxiss.app.role.service;

import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import com.gmail.llemaxiss.app.role.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Role getRoleById(@NotNull UUID id) throws EntityNotFoundException {
    Optional<Role> roleOptional = roleRepository.findById(id);

    if (roleOptional.isEmpty()) {
      String message = String.format("Role by id %s not found", id);
      throw new EntityNotFoundException(message);
    }

    return roleOptional.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Set<Role> getRolesByType(@NotNull RoleType roleType) {
    return roleRepository.findByType(roleType);
  }
}
