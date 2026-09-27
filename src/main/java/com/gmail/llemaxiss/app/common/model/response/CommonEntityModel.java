package com.gmail.llemaxiss.app.common.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Schema(description = "Base model containing common entity fields (audit, id)")
public abstract class CommonEntityModel {
  
  @Schema(description = "Entity ID", example = "10000000-0000-0000-0000-000000000001")
  protected UUID id;
  
  @Schema(description = "Create timestamp", example = "2026-01-01T00:00:00Z")
  protected Instant createTs;
  
  @Schema(description = "Created by", example = "admin")
  protected String createdBy;
  
  @Schema(description = "Update timestamp", example = "2026-01-01T00:00:00Z")
  protected Instant updateTs;
  
  @Schema(description = "Updated by", example = "admin")
  protected String updatedBy;
  
}
