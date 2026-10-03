package com.gmail.llemaxiss.app.userProfile.model.response;

import com.gmail.llemaxiss.app._common.model.response.CommonEntityModel;
import com.gmail.llemaxiss.app.user.model.response.UserIdModel;
import com.gmail.llemaxiss.app.userProfile.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Schema(description = "Response model containing user profile information")
public class UserProfileModel extends CommonEntityModel {
  
  @NotNull
  @Schema(description = "User associated with this profile")
  private UserIdModel user;
  
  @Schema(description = "User's first name", example = "Ivan")
  private String firstName;
  
  @Schema(description = "User's last name", example = "Ivanov")
  private String lastName;
  
  @Schema(description = "User's middle name (patronymic)", example = "Ivanovich")
  private String middleName;
  
  @Schema(description = "User's date of birth", example = "2026-01-01")
  private LocalDate dateOfBirth;
  
  @Schema(description = "User's gender", example = "MALE")
  private Gender gender;
  
}