package com.gmail.llemaxiss.app.userProfile.mapper;

import com.gmail.llemaxiss.app._common.mapper.CommonMapper;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.model.response.UserIdModel;
import com.gmail.llemaxiss.app.userProfile.entity.UserProfile;
import com.gmail.llemaxiss.app.userProfile.model.request.UserProfileCreateModel;
import com.gmail.llemaxiss.app.userProfile.model.request.UserProfileUpdateModel;
import com.gmail.llemaxiss.app.userProfile.model.response.UserProfileModel;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserProfileMapper {
  
  @NotNull
  public static UserProfileModel toModelFromEntity(@NotNull UserProfile userProfile) {
    UserProfileModel model = new UserProfileModel();
    
    CommonMapper.mapEntityFields(model, userProfile);
    
    UserIdModel userModel = new UserIdModel();
    CommonMapper.mapEntityId(userModel, userProfile.getUser());
    model.setUser(userModel);
    
    model.setFirstName(userProfile.getFirstName());
    model.setLastName(userProfile.getLastName());
    model.setMiddleName(userProfile.getMiddleName());
    model.setDateOfBirth(userProfile.getDateOfBirth());
    model.setGender(userProfile.getGender());
    
    return model;
  }
  
  public static void fillEntityByUserProfileCreateModel(@NotNull UserProfile userProfile,
                                                        @NotNull UserProfileCreateModel model,
                                                        @NotNull EntityManager em) {
    User user = em.getReference(User.class, model.getUser().getId());
    userProfile.setUser(user);
    
    userProfile.setFirstName(model.getFirstName());
    userProfile.setLastName(model.getLastName());
    userProfile.setMiddleName(model.getMiddleName());
    
    userProfile.setDateOfBirth(model.getDateOfBirth());
    
    userProfile.setGender(model.getGender());
  }
  
  public static void fillEntityByUserProfileUpdateModel(@NotNull UserProfile userProfile,
                                                        @NotNull UserProfileUpdateModel model,
                                                        @NotNull EntityManager em) {
    User user = em.getReference(User.class, model.getUser().getId());
    userProfile.setUser(user);
    
    userProfile.setFirstName(model.getFirstName());
    userProfile.setLastName(model.getLastName());
    userProfile.setMiddleName(model.getMiddleName());
    
    userProfile.setDateOfBirth(model.getDateOfBirth());
    
    userProfile.setGender(model.getGender());
  }
}
