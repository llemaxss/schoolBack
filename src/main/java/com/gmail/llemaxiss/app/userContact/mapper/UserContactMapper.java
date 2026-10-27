package com.gmail.llemaxiss.app.userContact.mapper;

import com.gmail.llemaxiss.app._common.mapper.CommonMapper;
import com.gmail.llemaxiss.app.userContact.entity.UserContact;
import com.gmail.llemaxiss.app.userContact.model.request.UserContactCreateModel;
import com.gmail.llemaxiss.app.userContact.model.request.UserContactUpdateModel;
import com.gmail.llemaxiss.app.userContact.model.response.UserContactModel;
import com.gmail.llemaxiss.app.userProfile.model.response.UserProfileIdModel;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserContactMapper {
  
  @NotNull
  public static UserContactModel toModelFromEntity(@NotNull UserContact userContact) {
    UserContactModel model = new UserContactModel();
    
    CommonMapper.mapEntityFields(model, userContact);
    
    UserProfileIdModel userProfileIdModel = new UserProfileIdModel();
    CommonMapper.mapEntityId(userProfileIdModel, userContact.getUserProfile());
    model.setUserProfile(userProfileIdModel);
    
    model.setType(userContact.getType());
    model.setValue(userContact.getValue());
    model.setIsPrimary(userContact.getIsPrimary());
    model.setComment(userContact.getComment());
    
    return model;
  }
  
  public static void fillEntityByUserContactCreateModel(@NotNull UserContact userContact,
                                                        @NotNull UserContactCreateModel model) {
    userContact.setType(model.getType());
    userContact.setValue(model.getValue());
    
    userContact.setIsPrimary(
      BooleanUtils.isTrue(model.getIsPrimary())
    );
    
    userContact.setComment(model.getComment());
  }
  
  public static void fillEntityByUserContactUpdateModel(@NotNull UserContact userContact,
                                                        @NotNull UserContactUpdateModel model) {
    userContact.setType(model.getType());
    userContact.setValue(model.getValue());
    
    userContact.setIsPrimary(
      BooleanUtils.isTrue(model.getIsPrimary())
    );
    
    userContact.setComment(model.getComment());
  }
  
}