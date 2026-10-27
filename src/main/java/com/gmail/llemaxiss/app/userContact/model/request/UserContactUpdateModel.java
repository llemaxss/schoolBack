package com.gmail.llemaxiss.app.userContact.model.request;

import com.gmail.llemaxiss.app.userContact.enums.ContactType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Schema(description = "Request model for updating an existing user contact")
public class UserContactUpdateModel {
  
  @Schema(
    description = "Contact id to update",
    example = "10000000-0000-0000-0000-000000000001",
    requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  private UUID id;
  
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
  
  @NotNull
  @Schema(
    description = "Is it the primary contact of this type for the user profile",
    example = "false",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private Boolean isPrimary;
  
  @Size(max = 500)
  @Schema(description = "Comment to the contact", example = "Work phone")
  private String comment;
  
}