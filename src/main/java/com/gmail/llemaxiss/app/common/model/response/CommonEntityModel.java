package com.gmail.llemaxiss.app.common.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class CommonEntityModel {
  
  protected UUID id;
  
  protected Instant createTs;
  
  protected String createdBy;
  
  protected Instant updateTs;
  
  protected String updatedBy;
  
}
