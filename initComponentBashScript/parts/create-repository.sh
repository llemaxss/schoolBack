#!/bin/bash

function create_repository() {
  local componentPath="$1/repository"
  local javaAppPackagePath="$2"
  local componentName="$3"
  local componentNameAsCamel="$4"
  local isEmptyClasses="$5"

  mkdir -p "${componentPath}"

  if [ "${isEmptyClasses}" == "true" ]; then
    touch "${componentPath}/${componentName}Repository.java"
  else
    cat > "${componentPath}/${componentName}Repository.java" <<EOF
package ${javaAppPackagePath}.${componentNameAsCamel}.repository;

import ${javaAppPackagePath}._common.repository.CommonRepository;
import ${javaAppPackagePath}.${componentNameAsCamel}.entity.${componentName};
import org.springframework.stereotype.Repository;

@Repository
public interface ${componentName}Repository extends CommonRepository<${componentName}> {

}
EOF
  fi
}
