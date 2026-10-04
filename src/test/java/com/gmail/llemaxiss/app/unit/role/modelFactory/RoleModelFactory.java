package com.gmail.llemaxiss.app.unit.role.modelFactory;

import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import com.gmail.llemaxiss.app.role.model.request.RoleCreateModel;
import com.gmail.llemaxiss.app.role.model.request.RoleUpdateModel;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public final class RoleModelFactory {
  
  public static final String ADMIN_NAME = "ADMIN";
  public static final RoleType ADMIN_TYPE = RoleType.ADMIN;
  
  public static final String TEACHER_NAME = "TEACHER";
  public static final RoleType TEACHER_TYPE = RoleType.TEACHER;
  
  private RoleModelFactory() {
  }
  
  public static Role adminEntity() {
    return entity(
      UUID.randomUUID(),
      ADMIN_NAME, ADMIN_TYPE
    );
  }
  
  public static Role teacherEntity() {
    return entity(
      UUID.randomUUID(),
      TEACHER_NAME, TEACHER_TYPE
    );
  }
  
  public static RoleCreateModel adminCreateModel() {
    return createModel(
      ADMIN_NAME, ADMIN_TYPE
    );
  }
  
  public static RoleUpdateModel adminUpdateModel() {
    return updateModel(
      UUID.randomUUID(),
      ADMIN_NAME, ADMIN_TYPE
    );
  }
  
  public static RoleUpdateModel teacherUpdateModel() {
    return updateModel(
      UUID.randomUUID(),
      TEACHER_NAME, TEACHER_TYPE
    );
  }
  
  public static Role entity(@NotNull UUID id, @NotNull String name, @NotNull RoleType type) {
    Role role = new Role();
    
    role.setId(id);
    role.setName(name);
    role.setType(type);
    
    return role;
  }
  
  public static RoleCreateModel createModel(@NotNull String name, @NotNull RoleType type) {
    RoleCreateModel model = new RoleCreateModel();
    
    model.setName(name);
    model.setType(type);
    
    return model;
  }
  
  public static RoleUpdateModel updateModel(@NotNull UUID id, @NotNull String name, @NotNull RoleType type) {
    RoleUpdateModel model = new RoleUpdateModel();
    
    model.setId(id);
    model.setName(name);
    model.setType(type);
    
    return model;
  }
  
}
