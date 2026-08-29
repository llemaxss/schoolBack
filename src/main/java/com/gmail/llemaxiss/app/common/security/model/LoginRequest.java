package com.gmail.llemaxiss.app.common.security.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginRequest implements Serializable {
  @NotEmpty
  private String username;

  @NotEmpty
  private String password;
}
