package com.gmail.llemaxiss.app._common.user.modelFactory;

import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.model.request.UserCreateModel;
import com.gmail.llemaxiss.app.user.model.request.UserUpdateModel;
import com.gmail.llemaxiss.app.user.model.response.UserIdModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Locale;
import java.util.UUID;

public final class UserModelFactory {
  
  public static final String IVAN_USERNAME = "ivan";
  public static final String ALEXANDRA_USERNAME = "alexandra";
  
  public static final String IVAN_PASSWORD = "Ivan-Password-1";
  public static final String ALEXANDRA_PASSWORD = "Alexandra-Password-1";
  
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
  
  public static UserCreateModel ivanCreateModel() {
    return createModel(
      IVAN_USERNAME,
      IVAN_PASSWORD
    );
  }
  
  public static UserUpdateModel ivanUpdateModel() {
    return updateModel(
      UUID.randomUUID(),
      IVAN_USERNAME
    );
  }
  
  public static UserUpdateModel alexandraUpdateModel() {
    return updateModel(
      UUID.randomUUID(),
      ALEXANDRA_USERNAME
    );
  }
  
  public static User entity(@NotNull UUID id, @NotBlank String username) {
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
  
  public static UserCreateModel createModel(@NotBlank String username, @NotBlank String password) {
    UserCreateModel model = new UserCreateModel();
    
    model.setUsername(username);
    model.setPassword(password);
    
    return model;
  }
  
  public static UserUpdateModel updateModel(@NotNull UUID id, @NotBlank String username) {
    UserUpdateModel model = new UserUpdateModel();
    
    model.setId(id);
    
    model.setUsername(username);
    
    return model;
  }
  
  public static String denormalizeUsername(@NotBlank String username) {
    return " " + username.toUpperCase(Locale.ROOT) + "  ";
  }
  
}
