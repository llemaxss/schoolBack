package com.gmail.llemaxiss.app.unit.user;

import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.model.response.UserIdModel;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public final class UserModelFactory {
  
  public static final String IVAN_USERNAME = "ivan";
  public static final String ALEXANDRA_USERNAME = "alexandra";
  
  public UserModelFactory() {
  }
  
  public static User ivanEntity() {
    return entity(
      UUID.randomUUID(),
      IVAN_USERNAME
    );
  }
  
  public static User alexandraEntity() {
    return entity(
      UUID.randomUUID(),
      ALEXANDRA_USERNAME
    );
  }
  
  public static UserIdModel userIdModel() {
    return userIdModel(
      UUID.randomUUID()
    );
  }
  
  public static User entity(@NotNull UUID id, @NotNull String username) {
    User user = new User();
    
    user.setId(id);
    
    user.setUsername(username);
    
    user.setIsActive(true);
    user.setPassword(
      UUID.randomUUID()
        .toString()
    );
    
    return user;
  }
  
  public static UserIdModel userIdModel(@NotNull UUID id) {
    UserIdModel model = new UserIdModel();
    
    model.setId(id);
    
    return model;
  }
  
}
