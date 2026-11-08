package com.gmail.llemaxiss.app.user.controller;

import com.gmail.llemaxiss.app._common.config.OpenApiConfig;
import com.gmail.llemaxiss.app._common.controller.CommonController;
import com.gmail.llemaxiss.app._common.model.response.ErrorResponse;
import com.gmail.llemaxiss.app._common.security.annotation.RequirePermission;
import com.gmail.llemaxiss.app.permission.enums.Permission;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.mapper.UserMapper;
import com.gmail.llemaxiss.app.user.model.request.UserCreateModel;
import com.gmail.llemaxiss.app.user.model.request.UserUpdateModel;
import com.gmail.llemaxiss.app.user.model.response.UserModel;
import com.gmail.llemaxiss.app.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.gmail.llemaxiss.app._common.component.AppProperty.API_URL_PART;

@RequiredArgsConstructor
@RestController
@RequestMapping(UserController.URL)
@Tag(
  name = "User",
  description = "Authenticated user management"
)
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class UserController extends CommonController {
  
  public static final String URL = API_URL_PART + "/users";
  
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
  
  @PostMapping
  @RequirePermission(Permission.CREATE_USER)
  @Operation(
    summary = "Create a new user",
    description = "Creates a new active user with the given temporary password"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "User created successfully",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserModel.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Validation failed or username already exists",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<UserModel> createUser(@Valid @RequestBody UserCreateModel model) {
    User user = userService.createUser(model);
    
    UserModel userModel = UserMapper.toModelFromEntity(user);
    
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(userModel);
  }
  
  @GetMapping("/{id}")
  @RequirePermission(Permission.READ_USER)
  @Operation(
    summary = "Get user by id",
    description = "Retrieves a specific user (including inactive) by its id"
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
      responseCode = "400",
      description = "User not found",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<UserModel> getUserById(
    @Parameter(
      name = "id",
      description = "User id",
      required = true,
      example = "10000000-0000-0000-0000-000000000001"
    )
    @PathVariable(name = "id") @NotNull UUID id
  ) {
    User user = userService.getUserByIdIncludingInactive(id);
    
    UserModel userModel = UserMapper.toModelFromEntity(user);
    
    return ResponseEntity.ok(userModel);
  }
  
  @PutMapping("/{id}")
  @RequirePermission(Permission.UPDATE_USER)
  @Operation(
    summary = "Update an existing user",
    description = "Updates the username of an existing user. Default users cannot be updated"
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
      responseCode = "400",
      description = "Validation failed, user not found, username already exists or user is protected",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<UserModel> updateUser(
    @Parameter(
      name = "id",
      description = "User id",
      required = true,
      example = "10000000-0000-0000-0000-000000000001"
    )
    @PathVariable(name = "id") @NotNull UUID id,
    @Valid @RequestBody UserUpdateModel model
  ) {
    User user = userService.updateUser(id, model);
    
    UserModel userModel = UserMapper.toModelFromEntity(user);
    
    return ResponseEntity.ok(userModel);
  }
  
  @DeleteMapping("/{id}")
  @RequirePermission(Permission.DELETE_USER)
  @Operation(
    summary = "Delete a user by id",
    description = "Soft deletes a user. Default users and the current user cannot be deleted"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "204",
      description = "User deleted successfully"
    ),
    @ApiResponse(
      responseCode = "400",
      description = "User not found, user is protected or it is the current user",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<Void> deleteUser(
    @Parameter(
      name = "id",
      description = "User id",
      required = true,
      example = "10000000-0000-0000-0000-000000000001"
    )
    @PathVariable(name = "id") @NotNull UUID id
  ) {
    userService.deleteUser(id);
    
    return ResponseEntity.noContent()
      .build();
  }
  
  @PostMapping("/{id}/activate")
  @RequirePermission(Permission.ACTIVATE_USER)
  @Operation(
    summary = "Activate a user",
    description = "Makes the user active"
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
      responseCode = "400",
      description = "User not found",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<UserModel> activateUser(
    @Parameter(
      name = "id",
      description = "User id",
      required = true,
      example = "10000000-0000-0000-0000-000000000001"
    )
    @PathVariable(name = "id") @NotNull UUID id
  ) {
    User user = userService.activateUser(id);
    
    UserModel userModel = UserMapper.toModelFromEntity(user);
    
    return ResponseEntity.ok(userModel);
  }
  
  @PostMapping("/{id}/deactivate")
  @RequirePermission(Permission.ACTIVATE_USER)
  @Operation(
    summary = "Deactivate a user",
    description = "Makes the user inactive. Default users and the current user cannot be deactivated"
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
      responseCode = "400",
      description = "User not found, user is protected or it is the current user",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<UserModel> deactivateUser(
    @Parameter(
      name = "id",
      description = "User id",
      required = true,
      example = "10000000-0000-0000-0000-000000000001"
    )
    @PathVariable(name = "id") @NotNull UUID id
  ) {
    User user = userService.deactivateUser(id);
    
    UserModel userModel = UserMapper.toModelFromEntity(user);
    
    return ResponseEntity.ok(userModel);
  }
  
}
