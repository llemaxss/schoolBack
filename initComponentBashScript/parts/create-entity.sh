#!/bin/bash

function create_entity() {
  local componentPath="$1/entity"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local isEmptyClasses="$5"

  local componentNameAsSnake
  componentNameAsSnake=$(get_component_name_as_snake "${componentName}")

  mkdir -p "${componentPath}"

  if [ "${isEmptyClasses}" == "true" ]; then
    touch "${componentPath}/${componentName}.java"
  else
    cat > "${componentPath}/${componentName}.java" <<EOF
package ${javaAppPackagePath}.${componentNameAsCamel}.entity;

import ${javaAppPackagePath}._common.entity.CommonEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static ${javaAppPackagePath}._common.component.AppProperty.TABLE_PREFIX;

@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = TABLE_PREFIX + "${componentNameAsSnake}")
public class ${componentName} extends CommonEntity {

}
EOF
  fi
}
