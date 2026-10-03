package com.gmail.llemaxiss.app.userProfile.controller;

import com.gmail.llemaxiss.app._common.config.OpenApiConfig;
import com.gmail.llemaxiss.app._common.controller.CommonController;
import com.gmail.llemaxiss.app._common.model.response.ErrorResponse;
import com.gmail.llemaxiss.app.userProfile.entity.UserProfile;
import com.gmail.llemaxiss.app.userProfile.mapper.UserProfileMapper;
import com.gmail.llemaxiss.app.userProfile.model.request.UserProfileCreateModel;
import com.gmail.llemaxiss.app.userProfile.model.request.UserProfileUpdateModel;
import com.gmail.llemaxiss.app.userProfile.model.response.UserProfileModel;
import com.gmail.llemaxiss.app.userProfile.service.UserProfileService;
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
@RequestMapping(API_URL_PART + "/user-profiles")
@Tag(
  name = "User profile",
  description = "User profile management"
)
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class UserProfileController extends CommonController {
  
  private final UserProfileService userProfileService;
  
  @PostMapping
  @Operation(
    summary = "Create a new user profile",
    description = "Creates a new user profile"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "User profile created successfully",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserProfileModel.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Validation failed or profile already exists",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<UserProfileModel> createUserProfile(@Valid @RequestBody UserProfileCreateModel model) {
    UserProfile userProfile = userProfileService.createUserProfile(model);
    
    UserProfileModel userProfileModel = UserProfileMapper.toModelFromEntity(userProfile);
    
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(userProfileModel);
  }
  
  @GetMapping("/{id}")
  @Operation(
    summary = "Get user profile by id",
    description = "Retrieves a specific profile by its id"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Successful operation",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserProfileModel.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "User profile not found",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<UserProfileModel> getUserProfileById(
    @Parameter(
      name = "id",
      description = "User profile id",
      required = true,
      example = "10000000-0000-0000-0000-000000000001"
    )
    @PathVariable(name = "id") @NotNull UUID id
  ) {
    UserProfile userProfile = userProfileService.getUserProfileById(id);
    
    UserProfileModel userProfileModel = UserProfileMapper.toModelFromEntity(userProfile);
    
    return ResponseEntity.ok(userProfileModel);
  }
  
  @PutMapping("/{id}")
  @Operation(
    summary = "Update an existing user profile",
    description = "Updates an existing user profile"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Successful operation",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserProfileModel.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Validation failed or profile already exists",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<UserProfileModel> updateUserProfile(
    @Parameter(
      name = "id",
      description = "User profile id",
      required = true,
      example = "10000000-0000-0000-0000-000000000001"
    )
    @PathVariable(name = "id") @NotNull UUID id,
    @Valid @RequestBody UserProfileUpdateModel model
  ) {
    UserProfile userProfile = userProfileService.updateUserProfile(id, model);
    
    UserProfileModel userProfileModel = UserProfileMapper.toModelFromEntity(userProfile);
    
    return ResponseEntity.ok(userProfileModel);
  }
  
  @DeleteMapping("/{id}")
  @Operation(
    summary = "Delete a user profile by id",
    description = "Soft deletes a role from the system"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "204",
      description = "User profile deleted successfully"
    ),
    @ApiResponse(
      responseCode = "400",
      description = "User profile not found",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<Void> deleteRole(
    @Parameter(
      name = "id",
      description = "User profile id",
      required = true,
      example = "10000000-0000-0000-0000-000000000001"
    )
    @PathVariable(name = "id") @NotNull UUID id
  ) {
    userProfileService.deleteUserProfile(id);
    
    return ResponseEntity.noContent()
      .build();
  }
  
}
