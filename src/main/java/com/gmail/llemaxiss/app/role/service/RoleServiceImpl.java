package com.gmail.llemaxiss.app.role.service;

import com.gmail.llemaxiss.app.common.enums.ErrorCode;
import com.gmail.llemaxiss.app.common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import com.gmail.llemaxiss.app.role.model.request.RoleCreateModel;
import com.gmail.llemaxiss.app.role.model.request.RoleUpdateModel;
import com.gmail.llemaxiss.app.role.repository.RoleRepository;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
  public Role getById(@NotNull UUID id) {
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
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional
  public Role create(@NotNull RoleCreateModel model) {
    validateName(model.getName());
    
    Role newRole = new Role();
    
    newRole.setName(model.getName());
    newRole.setType(model.getType());
    
    newRole = roleRepository.save(newRole);
    
    log.info("Role created successfully with id: {}", newRole.getId());
    
    return newRole;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional
  public Role update(@NotNull UUID id, @NotNull RoleUpdateModel model) {
    log.info("Updating role with id: {}", id);
    
    Role role = getById(id);
    
    boolean isNameChanged = !role.getName().equalsIgnoreCase(model.getName());
    
    if (isNameChanged) {
      validateName(model.getName());
    }
    
    role.setName(model.getName());
    role.setType(model.getType());
    
    Role updatedRole = roleRepository.save(role);
    
    log.info("Role updated successfully with id: {}", updatedRole.getId());
    
    return updatedRole;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void delete(@NotNull UUID id) {
    log.info("Deleting role with id: {}", id);
    
    Role role = getById(id);
    
    roleRepository.delete(role);
    
    log.info("Role deleted successfully with id: {}", id);
  }
  
  private void validateName(@NotNull String name) {
    boolean isExistsByName = roleRepository.existsByName(name);
    
    if (isExistsByName) {
      String message = String.format("Role with name %s already exists", name);
      throw new CommonException(ErrorCode.ROLE_NAME_ALREADY_EXISTS, message);
    }
  }
}
