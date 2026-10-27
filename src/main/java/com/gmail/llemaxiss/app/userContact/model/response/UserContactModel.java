package com.gmail.llemaxiss.app.userContact.model.response;

import com.gmail.llemaxiss.app._common.model.response.CommonEntityModel;
import com.gmail.llemaxiss.app.userContact.enums.ContactType;
import com.gmail.llemaxiss.app.userProfile.model.response.UserProfileIdModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Schema(description = "Response model containing user contact information")
public class UserContactModel extends CommonEntityModel {
  
  @NotNull
  @Schema(description = "User profile associated with this contact")
  private UserProfileIdModel userProfile;
  
  @Schema(description = "Contact type", example = "PHONE")
  private ContactType type;
  
  @Schema(description = "Contact value: phone number or e-mail address", example = "+79001234567")
  private String value;
  
  @Schema(description = "Is it the primary contact of this type for the user profile", example = "false")
  private Boolean isPrimary;
  
  @Schema(description = "Comment to the contact", example = "Work phone")
  private String comment;
  
}