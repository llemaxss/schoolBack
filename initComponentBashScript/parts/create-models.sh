#!/bin/bash

function create_models() {
  local componentPath="$1/model"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local isEmptyClasses="$5"

  local componentNameByWords
  componentNameByWords=$(get_component_name_by_words "${componentName}")

  _create_request_models "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${componentNameByWords}" "${isEmptyClasses}"
  _create_response_models "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${componentNameByWords}" "${isEmptyClasses}"
}

function _create_request_models() {
  local componentPath="$1/request"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local componentNameByWords="$5"
  local isEmptyClasses="$6"

  _create_request_create_model "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${componentNameByWords}" "${isEmptyClasses}"
  _create_request_update_model "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${componentNameByWords}" "${isEmptyClasses}"
}

function _create_response_models() {
  local componentPath="$1/response"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local componentNameByWords="$5"
  local isEmptyClasses="$6"

  _create_response_id_model "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${componentNameByWords}" "${isEmptyClasses}"
  _create_response_model "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${componentNameByWords}" "${isEmptyClasses}"
}

function _create_request_create_model() {
  local componentPath="$1"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local componentNameByWords="$5"
  local isEmptyClasses="$6"

  mkdir -p "${componentPath}"

  if [ "${isEmptyClasses}" == "true" ]; then
    touch "${componentPath}/${componentName}CreateModel.java"
  else
    cat > "${componentPath}/${componentName}CreateModel.java" <<EOF
package ${javaAppPackagePath}.${componentNameAsCamel}.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Schema(description = "Request model for creating a new ${componentNameByWords}")
public class ${componentName}CreateModel {

}
EOF
  fi
}

function _create_request_update_model() {
  local componentPath="$1"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local componentNameByWords="$5"
  local isEmptyClasses="$6"

  mkdir -p "${componentPath}"

  if [ "${isEmptyClasses}" == "true" ]; then
    touch "${componentPath}/${componentName}UpdateModel.java"
  else
    cat > "${componentPath}/${componentName}UpdateModel.java" <<EOF
package ${javaAppPackagePath}.${componentNameAsCamel}.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Schema(description = "Request model for updating an existing ${componentNameByWords}")
public class ${componentName}UpdateModel {

}
EOF
  fi
}

function _create_response_id_model() {
  local componentPath="$1"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local componentNameByWords="$5"
  local isEmptyClasses="$6"

  mkdir -p "${componentPath}"

  if [ "${isEmptyClasses}" == "true" ]; then
    touch "${componentPath}/${componentName}IdModel.java"
  else
    cat > "${componentPath}/${componentName}IdModel.java" <<EOF
package ${javaAppPackagePath}.${componentNameAsCamel}.model.response;

import ${javaAppPackagePath}._common.model.response.CommonEntityIdModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Schema(description = "Response model containing only ${componentNameByWords} id")
public class ${componentName}IdModel extends CommonEntityIdModel {
}
EOF
  fi
}

function _create_response_model() {
  local componentPath="$1"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local componentNameByWords="$5"
  local isEmptyClasses="$6"

  mkdir -p "${componentPath}"

  if [ "${isEmptyClasses}" == "true" ]; then
    touch "${componentPath}/${componentName}Model.java"
  else
    cat > "${componentPath}/${componentName}Model.java" <<EOF
package ${javaAppPackagePath}.${componentNameAsCamel}.model.response;

import ${javaAppPackagePath}._common.model.response.CommonEntityModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Schema(description = "Response model containing ${componentNameByWords} information")
public class ${componentName}Model extends CommonEntityModel {

}
EOF
  fi
}
