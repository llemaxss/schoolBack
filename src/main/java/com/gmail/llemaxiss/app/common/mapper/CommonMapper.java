package com.gmail.llemaxiss.app.common.mapper;

import com.gmail.llemaxiss.app.common.entity.CommonEntity;
import com.gmail.llemaxiss.app.common.model.response.CommonEntityModel;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonMapper {

  public static void mapEntityId(@NotNull CommonEntityModel model, @NotNull CommonEntity entity) {
    model.setId(entity.getId());
  }
  
  public static void mapEntityFields(@NotNull CommonEntityModel model, @NotNull CommonEntity entity) {
    mapEntityId(model, entity);
    
    model.setCreateTs(entity.getCreateTs());
    model.setCreatedBy(entity.getCreatedBy());
    
    model.setUpdateTs(entity.getUpdateTs());
    model.setUpdatedBy(entity.getUpdatedBy());
  }

}
