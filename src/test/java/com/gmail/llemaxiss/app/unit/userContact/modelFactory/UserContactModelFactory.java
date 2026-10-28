package com.gmail.llemaxiss.app.unit.userContact.modelFactory;

import com.gmail.llemaxiss.app.unit.userProfile.modelFactory.UserProfileModelFactory;
import com.gmail.llemaxiss.app.userContact.entity.UserContact;
import com.gmail.llemaxiss.app.userContact.enums.ContactType;
import com.gmail.llemaxiss.app.userContact.model.request.UserContactCreateModel;
import com.gmail.llemaxiss.app.userContact.model.request.UserContactUpdateModel;
import com.gmail.llemaxiss.app.userProfile.entity.UserProfile;
import com.gmail.llemaxiss.app.userProfile.model.response.UserProfileIdModel;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public final class UserContactModelFactory {
  
  public static final String INCREASE_VALUE = "1234567";
  public static final String DECREASE_VALUE = "7654321";
  
  public static final String PHONE_PREFIX = "+7900";
  public static final String EMAIL_SUFFIX = "@example.com";
  
  public static final String PHONE_INCREASE_VALUE = PHONE_PREFIX + INCREASE_VALUE;
  public static final String PHONE_DECREASE_VALUE = PHONE_PREFIX + DECREASE_VALUE;
  
  public static final String IVAN_EMAIL_VALUE = "ivan" + EMAIL_SUFFIX;
  public static final String ALEXANDRA_EMAIL_VALUE = "alexandra" + EMAIL_SUFFIX;
  
  public static final String COMMENT_INCREASE_VALUE = "Comment " + INCREASE_VALUE;
  public static final String COMMENT_DECREASE_VALUE = "Comment " + DECREASE_VALUE;
  
  public UserContactModelFactory() {
  }
  
  public static UserContact ivanPhoneEntity() {
    return entity(
      UUID.randomUUID(),
      UserProfileModelFactory.ivanEntity(),
      ContactType.PHONE, PHONE_INCREASE_VALUE,
      false,
      COMMENT_INCREASE_VALUE
    );
  }
  
  public static UserContact ivanEmailEntity() {
    return entity(
      UUID.randomUUID(),
      UserProfileModelFactory.ivanEntity(),
      ContactType.EMAIL, IVAN_EMAIL_VALUE,
      false,
      COMMENT_INCREASE_VALUE
    );
  }
  
  public static UserContact alexandraPhoneEntity() {
    return entity(
      UUID.randomUUID(),
      UserProfileModelFactory.alexandraEntity(),
      ContactType.PHONE, PHONE_DECREASE_VALUE,
      false,
      COMMENT_DECREASE_VALUE
    );
  }
  
  public static UserContact alexandraEmailEntity() {
    return entity(
      UUID.randomUUID(),
      UserProfileModelFactory.alexandraEntity(),
      ContactType.EMAIL, ALEXANDRA_EMAIL_VALUE,
      false,
      COMMENT_DECREASE_VALUE
    );
  }
  
  public static UserContactCreateModel ivanPhoneCreateModel() {
    return createModel(
      UserProfileModelFactory.userProfileIdModel(),
      ContactType.PHONE, PHONE_INCREASE_VALUE,
      false,
      COMMENT_INCREASE_VALUE
    );
  }
  
  public static UserContactCreateModel ivanEmailCreateModel() {
    return createModel(
      UserProfileModelFactory.userProfileIdModel(),
      ContactType.EMAIL, IVAN_EMAIL_VALUE,
      false,
      COMMENT_INCREASE_VALUE
    );
  }
  
  public static UserContactUpdateModel ivanPhoneUpdateModel() {
    return updateModel(
      UUID.randomUUID(),
      ContactType.PHONE, PHONE_INCREASE_VALUE,
      false,
      COMMENT_INCREASE_VALUE
    );
  }
  
  public static UserContactUpdateModel ivanEmailUpdateModel() {
    return updateModel(
      UUID.randomUUID(),
      ContactType.EMAIL, IVAN_EMAIL_VALUE,
      false,
      COMMENT_INCREASE_VALUE
    );
  }
  
  public static UserContactUpdateModel alexandraPhoneUpdateModel() {
    return updateModel(
      UUID.randomUUID(),
      ContactType.PHONE, PHONE_DECREASE_VALUE,
      false,
      COMMENT_DECREASE_VALUE
    );
  }
  
  public static UserContactUpdateModel alexandraEmailUpdateModel() {
    return updateModel(
      UUID.randomUUID(),
      ContactType.EMAIL, ALEXANDRA_EMAIL_VALUE,
      false,
      COMMENT_DECREASE_VALUE
    );
  }
  
  public static UserContact entity(@NotNull UUID id, @NotNull UserProfile userProfile,
                                   @NotNull ContactType type, @NotNull String value,
                                   @NotNull Boolean isPrimary,
                                   String comment) {
    UserContact userContact = new UserContact();
    
    userContact.setId(id);
    
    userContact.setUserProfile(userProfile);
    
    userContact.setType(type);
    userContact.setValue(value);
    userContact.setIsPrimary(isPrimary);
    userContact.setComment(comment);
    
    return userContact;
  }
  
  public static UserContactCreateModel createModel(@NotNull UserProfileIdModel userProfile,
                                                   @NotNull ContactType type, @NotNull String value,
                                                   @NotNull Boolean isPrimary,
                                                   String comment) {
    UserContactCreateModel model = new UserContactCreateModel();
    
    model.setUserProfile(userProfile);
    
    model.setType(type);
    model.setValue(value);
    model.setIsPrimary(isPrimary);
    model.setComment(comment);
    
    return model;
  }
  
  public static UserContactUpdateModel updateModel(@NotNull UUID id,
                                                   @NotNull ContactType type, @NotNull String value,
                                                   @NotNull Boolean isPrimary,
                                                   String comment) {
    UserContactUpdateModel model = new UserContactUpdateModel();
    
    model.setId(id);
    
    model.setType(type);
    model.setValue(value);
    model.setIsPrimary(isPrimary);
    model.setComment(comment);
    
    return model;
  }
  
}