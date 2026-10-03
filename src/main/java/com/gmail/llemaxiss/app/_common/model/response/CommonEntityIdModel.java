package com.gmail.llemaxiss.app._common.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
@Setter
@Schema(description = "Base model containing entity id")
public abstract class CommonEntityIdModel {
  
  @Schema(description = "Entity id", example = "10000000-0000-0000-0000-000000000001")
  protected UUID id;
  
}
