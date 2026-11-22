#!/bin/bash

scriptDir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

source "${scriptDir}/initComponentBashScript/init-component.sh"

isMakeMigrations="false"
isMakeClasses="false"

isEmptyMigration="false"
isEmptyClasses="false"

while getopts "mce:" opt; do
  case $opt in
    m) isMakeMigrations="true" ;;
    c) isMakeClasses="true" ;;
    e)
      case "$OPTARG" in
        m|migration|migrations)
          isEmptyMigration="true"
          ;;
        c|class|classes)
          isEmptyClasses="true"
          ;;
        a|all)
          isEmptyMigration="true"
          isEmptyClasses="true"
          ;;
        *)
          echo "Invalid argument for -e: '$OPTARG'. Use 'm', 'c', or 'a'"
          echo "💡 Example: ./create-component.sh -m -e c SomeComponentName"
          exit 1
          ;;
      esac
      ;;
    \?)
      echo "Invalid option: -$OPTARG"
      echo "Usage: $0 [-m] [-c] [-e m|c|a] SomeComponentName"
      exit 1
      ;;
  esac
done

shift $((OPTIND-1))
componentName="$1"

if [ -z "$1" ]; then
  echo "Name parameter is empty!"
  echo "For example: ./create-component [-m] [-c] [-e m|c|a] SomeComponentName"

  exit 1
fi

init_component "${componentName}" "${isMakeMigrations}" "${isMakeClasses}" "${isEmptyMigration}" "${isEmptyClasses}"