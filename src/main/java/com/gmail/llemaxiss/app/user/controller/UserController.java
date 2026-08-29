package com.gmail.llemaxiss.app.user.controller;

import com.gmail.llemaxiss.app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gmail.llemaxiss.app.common.property.component.AppProperty.API_URL_PART;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_URL_PART + "/users")
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<?> getCurrentUser() {
    try {
      UserDetails userDetails = userService.getCurrentUserDetails();

      return ResponseEntity.ok(userDetails);
    } catch (Exception e) {
      HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

      return ResponseEntity.status(httpStatus)
        .body(httpStatus.getReasonPhrase());
    }
  }
}
