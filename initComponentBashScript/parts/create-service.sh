#!/bin/bash

function create_service() {
  local componentPath="$1/service"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local isEmptyClasses="$5"

  mkdir -p "${componentPath}"

  _create_service_interface "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${isEmptyClasses}"
  _create_service_interface_impl "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${isEmptyClasses}"
}

function _create_service_interface() {
  local componentPath="$1"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local isEmptyClasses="$5"

  if [ "${isEmptyClasses}" == "true" ]; then
    touch "${componentPath}/${componentName}Service.java"
  else
    cat > "${componentPath}/${componentName}Service.java" <<EOF
package ${javaAppPackagePath}.${componentNameAsCamel}.service;

public interface ${componentName}Service {
}
EOF
  fi
}

function _create_service_interface_impl() {
  local componentPath="$1"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local isEmptyClasses="$5"

  if [ "${isEmptyClasses}" == "true" ]; then
    touch "${componentPath}/${componentName}ServiceImpl.java"
  else
    cat > "${componentPath}/${componentName}ServiceImpl.java" <<EOF
package ${javaAppPackagePath}.${componentNameAsCamel}.service;

import ${javaAppPackagePath}._common.service.CommonService;
import ${javaAppPackagePath}.${componentNameAsCamel}.repository.${componentName}Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ${componentName}ServiceImpl extends CommonService implements ${componentName}Service {

  private final ${componentName}Repository ${componentNameAsCamel}Repository;

}
EOF
  fi
}