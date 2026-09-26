package com.gmail.llemaxiss.app.role.controller;

import com.gmail.llemaxiss.app.common.model.response.ErrorResponse;
import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.mapper.RoleMapper;
import com.gmail.llemaxiss.app.role.model.request.RoleCreateModel;
import com.gmail.llemaxiss.app.role.model.request.RoleUpdateModel;
import com.gmail.llemaxiss.app.role.model.response.RoleModel;
import com.gmail.llemaxiss.app.role.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import static com.gmail.llemaxiss.app.common.property.component.AppProperty.API_URL_PART;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_URL_PART + "/roles")
@Tag(
  name = "Role",
  description = "Role management"
)
public class RoleController {
  
  private final RoleService roleService;
  
  @PostMapping
  @Operation(
    summary = "Create a new role",
    description = "Creates a new role with the specified name and type"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Role created successfully",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = RoleModel.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Validation failed or role name already exists",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<RoleModel> createRole(@Valid @RequestBody RoleCreateModel model) {
    Role newRole = roleService.create(model);
    
    RoleModel roleModel = RoleMapper.toModelFromEntity(newRole);
    
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(roleModel);
  }
  
  @GetMapping("/{id}")
  @Operation(
    summary = "Get role by ID",
    description = "Retrieves a specific role by its id"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Successful operation",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = RoleModel.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Role not found",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<RoleModel> getRoleById(@PathVariable(name = "id") @NotNull UUID id) {
    Role role = roleService.getById(id);
    
    RoleModel roleModel = RoleMapper.toModelFromEntity(role);
    
    return ResponseEntity.ok(roleModel);
  }
  
  @PutMapping("/{id}")
  @Operation(
    summary = "Update an existing role",
    description = "Partially or fully updates an existing role"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Successful operation",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = RoleModel.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Validation failed, role not found, or name already exists",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<RoleModel> updateRole(
    @PathVariable(name = "id") @NotNull UUID id,
    @Valid @RequestBody RoleUpdateModel model
  ) {
    Role role = roleService.update(id, model);
    
    RoleModel roleModel = RoleMapper.toModelFromEntity(role);
    
    return ResponseEntity.ok(roleModel);
  }
  
  @DeleteMapping("/{id}")
  @Operation(
    summary = "Delete a role by ID",
    description = "Permanently deletes a role from the system."
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "204",
      description = "Role deleted successfully"
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Role not found",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)
      )
    )
  })
  public ResponseEntity<Void> deleteRole(@PathVariable(name = "id") @NotNull UUID id) {
    roleService.delete(id);
    
    return ResponseEntity.noContent().build();
  }
  
}
