package com.gmail.llemaxiss.app.unit.userProfile.modelFactory;

import com.gmail.llemaxiss.app.unit.user.UserModelFactory;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.model.response.UserIdModel;
import com.gmail.llemaxiss.app.userProfile.entity.UserProfile;
import com.gmail.llemaxiss.app.userProfile.enums.Gender;
import com.gmail.llemaxiss.app.userProfile.model.request.UserProfileCreateModel;
import com.gmail.llemaxiss.app.userProfile.model.request.UserProfileUpdateModel;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public final class UserProfileModelFactory {
  
  public static final String IVAN_FIRST_NAME = "Ivan";
  public static final String IVANOV_LAST_NAME = "Ivanov";
  public static final String IVANOVICH_MIDDLE_NAME = "Ivanovich";
  
  public static final String ALEXANDRA_FIRST_NAME = "Alexandra";
  public static final String ALEXANDROVA_LAST_NAME = "Alexandrova";
  public static final String ALEXANDROVNA_MIDDLE_NAME = "Alexandrovna";
  
  public static final LocalDate DATE_OF_BIRTH_2026_01_01 = LocalDate.of(2026, 1, 1);
  public static final LocalDate DATE_OF_BIRTH_2026_12_31 = LocalDate.of(2026, 12, 31);
  
  public static final Gender GENDER_MALE = Gender.MALE;
  public static final Gender GENDER_FEMALE = Gender.FEMALE;
  
  public UserProfileModelFactory() {
  }
  
  public static UserProfile ivanEntity() {
    return entity(
      UUID.randomUUID(),
      UserModelFactory.entity(),
      IVAN_FIRST_NAME, IVANOV_LAST_NAME, IVANOVICH_MIDDLE_NAME,
      DATE_OF_BIRTH_2026_01_01,
      GENDER_MALE
    );
  }
  
  public static UserProfile alexandraEntity() {
    return entity(
      UUID.randomUUID(),
      UserModelFactory.entity(),
      ALEXANDRA_FIRST_NAME, ALEXANDROVA_LAST_NAME, ALEXANDROVNA_MIDDLE_NAME,
      DATE_OF_BIRTH_2026_12_31,
      GENDER_FEMALE
    );
  }
  
  public static UserProfileCreateModel ivanCreateModel() {
    return createModel(
      UserModelFactory.userIdModel(),
      IVAN_FIRST_NAME, IVANOV_LAST_NAME, IVANOVICH_MIDDLE_NAME,
      DATE_OF_BIRTH_2026_01_01,
      GENDER_MALE
    );
  }
  
  public static UserProfileUpdateModel ivanUpdateModel() {
    return updateModel(
      UUID.randomUUID(),
      UserModelFactory.userIdModel(),
      IVAN_FIRST_NAME, IVANOV_LAST_NAME, IVANOVICH_MIDDLE_NAME,
      DATE_OF_BIRTH_2026_01_01,
      GENDER_MALE
    );
  }
  
  public static UserProfileUpdateModel alexandraUpdateModel() {
    return updateModel(
      UUID.randomUUID(),
      UserModelFactory.userIdModel(),
      ALEXANDRA_FIRST_NAME, ALEXANDROVA_LAST_NAME, ALEXANDROVNA_MIDDLE_NAME,
      DATE_OF_BIRTH_2026_12_31,
      GENDER_FEMALE
    );
  }
  
  public static UserProfile entity(@NotNull UUID id, @NotNull User user,
                                   @NotNull String firstName, @NotNull String lastName, String middleName,
                                   @NotNull LocalDate dateOfBirth,
                                   @NotNull Gender gender) {
    UserProfile userProfile = new UserProfile();
    
    userProfile.setId(id);
    
    userProfile.setUser(user);
    
    userProfile.setFirstName(firstName);
    userProfile.setLastName(lastName);
    userProfile.setMiddleName(middleName);
    
    userProfile.setDateOfBirth(dateOfBirth);
    
    userProfile.setGender(gender);
    
    return userProfile;
  }
  
  public static UserProfileCreateModel createModel(@NotNull UserIdModel userModel,
                                                   @NotNull String firstName, @NotNull String lastName, String middleName,
                                                   @NotNull LocalDate dateOfBirth,
                                                   @NotNull Gender gender) {
    UserProfileCreateModel model = new UserProfileCreateModel();
    
    model.setUser(userModel);
    
    model.setFirstName(firstName);
    model.setLastName(lastName);
    model.setMiddleName(middleName);
    
    model.setDateOfBirth(dateOfBirth);
    
    model.setGender(gender);
    
    return model;
  }
  
  public static UserProfileUpdateModel updateModel(@NotNull UUID id, @NotNull UserIdModel userModel,
                                                   @NotNull String firstName, @NotNull String lastName, String middleName,
                                                   @NotNull LocalDate dateOfBirth,
                                                   @NotNull Gender gender) {
    UserProfileUpdateModel model = new UserProfileUpdateModel();
    
    model.setId(id);
    
    model.setUser(userModel);
    
    model.setFirstName(firstName);
    model.setLastName(lastName);
    model.setMiddleName(middleName);
    
    model.setDateOfBirth(dateOfBirth);
    
    model.setGender(gender);
    
    return model;
  }
  
}
