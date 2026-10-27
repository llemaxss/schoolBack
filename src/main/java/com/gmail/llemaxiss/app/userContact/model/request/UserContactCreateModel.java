package com.gmail.llemaxiss.app.userContact.model.request;

import com.gmail.llemaxiss.app.userContact.enums.ContactType;
import com.gmail.llemaxiss.app.userProfile.model.response.UserProfileIdModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Schema(description = "Request model for creating a new user contact")
public class UserContactCreateModel {
  
  @NotNull
  @Schema(
    description = "User profile associated with this contact",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private UserProfileIdModel userProfile;
  
  @NotNull
  @Schema(
    description = "Contact type",
    example = "PHONE",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private ContactType type;
  
  @NotEmpty
  @Size(max = 255)
  @Schema(
    description = "Contact value: phone number or e-mail address",
    example = "+79001234567",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private String value;
  
  @Schema(
    description = "Is it the primary contact of this type for the user profile (false by default)",
    example = "false",
    requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  private Boolean isPrimary = false;
  
  @Size(max = 500)
  @Schema(description = "Comment to the contact", example = "Work phone")
  private String comment;
  
}