package com.gmail.llemaxiss.app.auth.service;

import com.gmail.llemaxiss.app._common.security.model.request.LoginRequest;
import com.gmail.llemaxiss.app._common.security.model.response.LoginResponse;
import jakarta.validation.constraints.NotNull;

public interface AuthService {
  
  /**
   * Get {@link LoginRequest}, checks and authenticate user in system,
   * then return logged in info as {@link LoginResponse}
   *
   * @param loginRequest is {@link LoginResponse}
   * @return logged in info
   */
  LoginResponse login(@NotNull LoginRequest loginRequest);
}
