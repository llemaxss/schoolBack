package com.gmail.llemaxiss.app.userContact.controller;

import com.gmail.llemaxiss.app._common.config.OpenApiConfig;
import com.gmail.llemaxiss.app._common.controller.CommonController;
import com.gmail.llemaxiss.app._common.model.response.ErrorResponse;
import com.gmail.llemaxiss.app._common.security.annotation.RequirePermission;
import com.gmail.llemaxiss.app.permission.enums.Permission;
import com.gmail.llemaxiss.app.userContact.entity.UserContact;
import com.gmail.llemaxiss.app.userContact.mapper.UserContactMapper;
import com.gmail.llemaxiss.app.userContact.model.request.UserContactCreateModel;
import com.gmail.llemaxiss.app.userContact.model.request.UserContactUpdateModel;
import com.gmail.llemaxiss.app.userContact.model.response.UserContactModel;
import com.gmail.llemaxiss.app.userContact.service.UserContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.gmail.llemaxiss.app._common.component.AppProperty.API_URL_PART;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_URL_PART + "/user-contacts")
@Tag(
  name = "User contact",
  description = "User contact (phones, e-mails) management"
)
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class UserContactController extends CommonController {
  
  private final UserContactService userContactService;
  
  @PostMapping
  @RequirePermission(Permission.UPDATE_USER_PROFILE)
  @Operation(
    summary = "Create a new user contact",
    description = "Creates a new contact (phone or e-mail) of the user profile"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "User contact created successfully",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserContactModel.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Validation failed, user profile not found or contact already exists",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<UserContactModel> createUserContact(@Valid @RequestBody UserContactCreateModel model) {
    UserContact userContact = userContactService.createUserContact(model);
    
    UserContactModel userContactModel = UserContactMapper.toModelFromEntity(userContact);
    
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(userContactModel);
  }
  
  @GetMapping("/{id}")
  @RequirePermission(Permission.READ_USER_PROFILE)
  @Operation(
    summary = "Get user contact by id",
    description = "Retrieves a specific user contact by its id"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Successful operation",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserContactModel.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "User contact not found",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<UserContactModel> getUserContactById(
    @Parameter(
      name = "id",
      description = "User contact id",
      required = true,
      example = "10000000-0000-0000-0000-000000000001"
    )
    @PathVariable(name = "id") @NotNull UUID id
  ) {
    UserContact userContact = userContactService.getUserContactById(id);
    
    UserContactModel userContactModel = UserContactMapper.toModelFromEntity(userContact);
    
    return ResponseEntity.ok(userContactModel);
  }
  
  @GetMapping
  @RequirePermission(Permission.READ_USER_PROFILE)
  @Operation(
    summary = "Get user contacts by user profile id",
    description = "Retrieves all contacts of the user profile (by type, primary first)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Successful operation",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        array = @ArraySchema(
          schema = @Schema(implementation = UserContactModel.class)
        )
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
  public ResponseEntity<List<UserContactModel>> getUserContactsByUserProfileId(
    @Parameter(
      name = "userProfileId",
      description = "User profile id",
      required = true,
      example = "10000000-0000-0000-0000-000000000001"
    )
    @RequestParam(name = "userProfileId") @NotNull UUID userProfileId
  ) {
    List<UserContact> userContacts = userContactService.getUserContactsByUserProfileId(userProfileId);
    
    List<UserContactModel> userContactModels = userContacts.stream()
      .map(UserContactMapper::toModelFromEntity)
      .toList();
    
    return ResponseEntity.ok(userContactModels);
  }
  
  @PutMapping("/{id}")
  @RequirePermission(Permission.UPDATE_USER_PROFILE)
  @Operation(
    summary = "Update an existing user contact",
    description = "Updates an existing user contact"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Successful operation",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserContactModel.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Validation failed, user contact not found or contact already exists",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<UserContactModel> updateUserContact(
    @Parameter(
      name = "id",
      description = "User contact id",
      required = true,
      example = "10000000-0000-0000-0000-000000000001"
    )
    @PathVariable(name = "id") @NotNull UUID id,
    @Valid @RequestBody UserContactUpdateModel model
  ) {
    UserContact userContact = userContactService.updateUserContact(id, model);
    
    UserContactModel userContactModel = UserContactMapper.toModelFromEntity(userContact);
    
    return ResponseEntity.ok(userContactModel);
  }
  
  @DeleteMapping("/{id}")
  @RequirePermission(Permission.UPDATE_USER_PROFILE)
  @Operation(
    summary = "Delete a user contact by id",
    description = "Soft deletes a user contact from the system"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "204",
      description = "User contact deleted successfully"
    ),
    @ApiResponse(
      responseCode = "400",
      description = "User contact not found",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<Void> deleteUserContact(
    @Parameter(
      name = "id",
      description = "User contact id",
      required = true,
      example = "10000000-0000-0000-0000-000000000001"
    )
    @PathVariable(name = "id") @NotNull UUID id
  ) {
    userContactService.deleteUserContact(id);
    
    return ResponseEntity.noContent()
      .build();
  }
  
}