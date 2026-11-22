#!/bin/bash

function create_controller() {
  local componentPath="$1/controller"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local isEmptyClasses="$5"

  local componentNameByWords
  componentNameByWords=$(get_component_name_by_words "${componentName}" "upper")

  local componentNameAsSnake
  componentNameAsSnake=$(get_component_name_as_snake "${componentName}" "upper")

  local componentNameAsSnakePlural
  componentNameAsSnakePlural=$(add_plural_suffix "${componentNameAsSnake}")

  mkdir -p "${componentPath}"

  if [ "${isEmptyClasses}" == "true" ]; then
    touch "${componentPath}/${componentName}Controller.java"
  else
    cat > "${componentPath}/${componentName}Controller.java" <<EOF
package ${javaAppPackagePath}.${componentNameAsCamel}.controller;

import ${javaAppPackagePath}._common.config.OpenApiConfig;
import ${javaAppPackagePath}._common.controller.CommonController;
import ${javaAppPackagePath}.${componentNameAsCamel}.service.${componentName}Service;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ${javaAppPackagePath}._common.component.AppProperty.API_URL_PART;

@RequiredArgsConstructor
@RestController
@RequestMapping(${componentName}Controller.URL)
@Tag(
  name = "${componentNameByWords}",
  description = "${componentNameByWords} management"
)
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class ${componentName}Controller extends CommonController {
  
  public static final String URL = API_URL_PART + "/${componentNameAsSnakePlural}";
  
  private final ${componentName}Service ${componentNameAsCamel}Service;
  
}
EOF
  fi
}
