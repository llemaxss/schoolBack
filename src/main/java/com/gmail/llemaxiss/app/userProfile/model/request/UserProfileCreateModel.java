package com.gmail.llemaxiss.app.userProfile.model.request;

import com.gmail.llemaxiss.app.user.model.response.UserIdModel;
import com.gmail.llemaxiss.app.userProfile.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Schema(description = "Request model for creating a new user profile")
public class UserProfileCreateModel {
  
  @NotNull
  @Schema(
    description = "User associated with this profile",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private UserIdModel user;
  
  @NotEmpty
  @Schema(
    description = "User's first name",
    example = "Ivan",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private String firstName;
  
  @NotEmpty
  @Schema(
    description = "User's last name",
    example = "Ivanov",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private String lastName;
  
  @Schema(description = "User's middle name (patronymic)", example = "Ivanovich")
  private String middleName;

  @NotNull
  @Schema(
    description = "User's date of birth",
    example = "2026-01-01",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private LocalDate dateOfBirth;

  @NotNull
  @Schema(
    description = "User's gender",
    example = "MALE",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private Gender gender;
  
}
