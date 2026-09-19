package com.gmail.llemaxiss.app.role.service;

import com.gmail.llemaxiss.app.common.enums.ErrorCode;
import com.gmail.llemaxiss.app.common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import com.gmail.llemaxiss.app.role.repository.RoleRepository;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional(readOnly = true)
  public Role getRoleById(@NotNull UUID id) {
    Optional<Role> roleOptional = roleRepository.findById(id);

    if (roleOptional.isEmpty()) {
      String message = String.format("Role by id %s not found", id);
      throw new CommonException(ErrorCode.ROLE_NOT_FOUND, message);
    }

    return roleOptional.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional(readOnly = true)
  public Set<Role> getRolesByType(@NotNull RoleType roleType) {
    return roleRepository.findByType(roleType);
  }
  
}
