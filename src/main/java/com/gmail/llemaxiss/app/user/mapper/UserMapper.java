package com.gmail.llemaxiss.app.user.mapper;

import com.gmail.llemaxiss.app._common.mapper.CommonMapper;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.model.request.UserCreateModel;
import com.gmail.llemaxiss.app.user.model.request.UserUpdateModel;
import com.gmail.llemaxiss.app.user.model.response.UserModel;
import com.gmail.llemaxiss.app.user.util.UserUtil;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

  @NotNull
  public static UserModel toModelFromEntity(@NotNull User user) {
    UserModel userModel = new UserModel();
    
    CommonMapper.mapEntityFields(userModel, user);
    
    userModel.setUsername(user.getUsername());
    userModel.setIsActive(user.getIsActive());
    
    userModel.setAuthorities(
      UserUtil.getAuthorities(user)
    );
    
    return userModel;
  }
  
  public static void fillEntityByUserCreateModel(@NotNull User user, @NotNull UserCreateModel model) {
    user.setUsername(model.getUsername());
  }
  
  public static void fillEntityByUserUpdateModel(@NotNull User user, @NotNull UserUpdateModel model) {
    user.setUsername(model.getUsername());
  }
  
}
