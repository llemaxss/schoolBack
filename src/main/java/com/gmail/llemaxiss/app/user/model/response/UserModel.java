package com.gmail.llemaxiss.app.user.model.response;

import com.gmail.llemaxiss.app._common.model.response.CommonEntityModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Response model containing user information")
public class UserModel extends CommonEntityModel {
  
  @NotNull
  @Schema(
    description = "User id",
    example = "10000000-0000-0000-0000-000000000001"
  )
  private UUID id;
  
  @NotEmpty
  @Schema(
    description = "User login",
    example = "admin"
  )
  private String username;
  
  @Schema(
    description = "User authorities",
    example = "[\"ROLE_CREATE\", \"ROLE_UPDATE\"]"
  )
  private Collection<? extends GrantedAuthority> authorities;
  
}
