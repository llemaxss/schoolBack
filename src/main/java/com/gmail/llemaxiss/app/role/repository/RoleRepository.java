package com.gmail.llemaxiss.app.role.repository;

import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

  Set<Role> findByType(@NotNull RoleType roleType);
  
}
