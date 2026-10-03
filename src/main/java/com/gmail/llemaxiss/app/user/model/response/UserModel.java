package com.gmail.llemaxiss.app.user.model.response;

import com.gmail.llemaxiss.app._common.model.response.CommonEntityModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Response model containing user information")
public class UserModel extends CommonEntityModel {
  
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
