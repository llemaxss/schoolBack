package com.gmail.llemaxiss.app.role.repository;

import com.gmail.llemaxiss.app.common.repository.CommonRepository;
import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CommonRepository<Role> {

  Set<Role> findByType(@NotNull RoleType roleType);
  
}
