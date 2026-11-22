#!/bin/bash

function create_mapper() {
  local componentPath="$1/mapper"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local isEmptyClasses="$5"

  mkdir -p "${componentPath}"

  if [ "${isEmptyClasses}" == "true" ]; then
    touch "${componentPath}/${componentName}Mapper.java"
  else
    cat > "${componentPath}/${componentName}Mapper.java" <<EOF
package ${javaAppPackagePath}.${componentNameAsCamel}.mapper;

import ${javaAppPackagePath}._common.mapper.CommonMapper;
import ${javaAppPackagePath}.${componentNameAsCamel}.entity.${componentName};
import ${javaAppPackagePath}.${componentNameAsCamel}.model.request.${componentName}CreateModel;
import ${javaAppPackagePath}.${componentNameAsCamel}.model.request.${componentName}UpdateModel;
import ${javaAppPackagePath}.${componentNameAsCamel}.model.response.${componentName}Model;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ${componentName}Mapper {
  
  @NotNull
  public static ${componentName}Model toModelFromEntity(@NotNull ${componentName} ${componentNameAsCamel}) {
    ${componentName}Model model = new ${componentName}Model();
    
    CommonMapper.mapEntityFields(model, ${componentNameAsCamel});
    
    return model;
  }
  
  public static void fillEntityBy${componentName}CreateModel(@NotNull ${componentName} ${componentNameAsCamel},
                                                             @NotNull ${componentName}CreateModel model) {
    
  }
  
  public static void fillEntityBy${componentName}UpdateModel(@NotNull ${componentName} ${componentNameAsCamel},
                                                             @NotNull ${componentName}UpdateModel model) {
    
  }
  
}
EOF
  fi
}
