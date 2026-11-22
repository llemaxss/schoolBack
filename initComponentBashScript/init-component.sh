#!/bin/bash

initScriptDir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

source "${initScriptDir}/parts/utils.sh"

source "${initScriptDir}/parts/create-migrations.sh"

source "${initScriptDir}/parts/create-controller.sh"
source "${initScriptDir}/parts/create-entity.sh"
source "${initScriptDir}/parts/create-mapper.sh"
source "${initScriptDir}/parts/create-models.sh"
source "${initScriptDir}/parts/create-repository.sh"
source "${initScriptDir}/parts/create-service.sh"

function init_component() {
  local componentName="$1"
  local isMakeMigrations="$2"
  local isMakeClasses="$3"
  local isEmptyMigration="$4"
  local isEmptyClasses="$5"

  local componentNameAsCamel
  componentNameAsCamel=$(lowercase_first_letter "${componentName}")

  local javaAppPackagePath="com.gmail.llemaxiss.app"
  local migrationPath="src/main/resources/db/changelog"

  local pathPart
  pathPart=$(_convert_dots_to_slashes "${javaAppPackagePath}")

  local componentPath="src/main/java/${pathPart}/${componentNameAsCamel}"

  echo "Starting component '${componentName}' creation..."

  if [ "${isMakeMigrations}" == "true" ]; then
    create_migrations "${migrationPath}" "${componentName}" "${isEmptyMigration}"
  fi

  if [ "${isMakeClasses}" == "true" ]; then
    create_controller "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${isEmptyClasses}"
    create_entity "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${isEmptyClasses}"
    create_mapper "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${isEmptyClasses}"
    create_models "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${isEmptyClasses}"
    create_repository "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${isEmptyClasses}"
    create_service "${componentPath}" "${javaAppPackagePath}" "${componentName}" "${componentNameAsCamel}" "${isEmptyClasses}"
  fi

  echo "Component '${componentName}' created successfully!"
}

function _convert_dots_to_slashes() {
  local package="$1"

  echo "${package}" | tr '.' '/'
}
