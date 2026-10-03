package com.gmail.llemaxiss.app.user.controller;

import com.gmail.llemaxiss.app._common.config.OpenApiConfig;
import com.gmail.llemaxiss.app._common.controller.CommonController;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.mapper.UserMapper;
import com.gmail.llemaxiss.app.user.model.response.UserModel;
import com.gmail.llemaxiss.app.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gmail.llemaxiss.app._common.component.AppProperty.API_URL_PART;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_URL_PART + "/users")
@Tag(
  name = "User",
  description = "Authenticated user management"
)
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class UserController extends CommonController {

  private final UserService userService;

  @GetMapping("/me")
  @Operation(
    summary = "Info about current user",
    description = "Return current authenticated user-details information"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Successful operation",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserModel.class)
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Unauthorized",
      content = @Content()
    )
  })
  public ResponseEntity<?> me() {
    try {
      User user = userService.getCurrentUser();
      
      UserModel userModel = UserMapper.toModelFromEntity(user);
      
      return ResponseEntity.ok(userModel);
    } catch (Exception e) {
      HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

      return ResponseEntity.status(httpStatus)
        .body(httpStatus.getReasonPhrase());
    }
  }
  
}
