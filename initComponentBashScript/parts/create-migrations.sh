#!/bin/bash

function create_migrations() {
  local migrationPath="$1"
  local componentName="$2"
  local isEmptyMigration="$3"

  local year
  year=$(get_current_year)

  local month
  month=$(get_current_month)

  local migrationYearPath="${migrationPath}/${year}"
  local migrationYearMonthPath="${migrationYearPath}/${month}"

  mkdir -p "${migrationYearMonthPath}"

  local nextFileNum
  nextFileNum=$(_get_next_file_num "${migrationYearMonthPath}")

  local createTableFileName
  createTableFileName=$(_get_create_file_name "${componentName}" "${nextFileNum}")

  local yearMonthChangelogFileName
  yearMonthChangelogFileName=$(_get_year_month_changelog_file_name)

  local yearChangelogFileName
  yearChangelogFileName=$(_get_year_changelog_file_name)

  local isYearMonthChangelogFileExists
  isYearMonthChangelogFileExists=$(is_file_exists  "${migrationYearMonthPath}/${yearMonthChangelogFileName}.yml")

  local isYearChangelogFileExists
  isYearChangelogFileExists=$(is_file_exists  "${migrationYearPath}/${yearChangelogFileName}.yml")

  _prepare_create_table_file "${migrationYearMonthPath}" "${componentName}" "${createTableFileName}" "${isEmptyMigration}"
  _prepare_year_month_changelog_file "${migrationYearMonthPath}" "${yearMonthChangelogFileName}" "${createTableFileName}" "${isYearMonthChangelogFileExists}"
  _prepare_year_changelog_file "${migrationYearPath}" "${yearChangelogFileName}" "${isYearChangelogFileExists}" "${yearMonthChangelogFileName}" "${isYearMonthChangelogFileExists}"
  _prepare_root_changelog_file "${migrationPath}" "${yearChangelogFileName}" "${isYearChangelogFileExists}"
}

function _prepare_create_table_file() {
  local migrationYearMonthPath="$1"
  local componentName="$2"
  local createTableFileName="$3"
  local isEmptyMigration="$4"

  local componentNameAsSnake
  componentNameAsSnake=$(get_component_name_as_snake "${componentName}")

  local componentNameByWords
  componentNameByWords=$(get_component_name_by_words "${componentName}")

  if [ "${isEmptyMigration}" == "true" ]; then
    touch "${migrationYearMonthPath}/${createTableFileName}.yml"
  else
    cat > "${migrationYearMonthPath}/${createTableFileName}.yml" <<EOF
databaseChangeLog:
  - changeSet:
      id: ${createTableFileName}
      author: llemaxss
      comment: Create ${componentNameByWords} table

      changes:
        - createTable:
            tableName: school_${componentNameAsSnake}
            columns:
              # common fields #
              - column:
                  name: id
                  type: uuid
                  constraints:
                    primaryKey: true
                    primaryKeyName: pk_${componentNameAsSnake}
                    nullable: false
              - column:
                  name: version
                  type: bigint
                  defaultValueNumeric: 1
                  constraints:
                    nullable: false
              - column:
                  name: create_ts
                  type: timestamp with time zone
                  defaultValueComputed: current_timestamp
                  constraints:
                    nullable: false
              - column:
                  name: created_by
                  type: varchar(255)
                  constraints:
                    nullable: false
              - column:
                  name: update_ts
                  type: timestamp with time zone
              - column:
                  name: updated_by
                  type: varchar(255)
              - column:
                  name: delete_ts
                  type: timestamp with time zone
              - column:
                  name: deleted_by
                  type: varchar(255)

              # other fields #
              - column:
                  name: FIX_ME
                  type: FIX_ME

        # indexes #
        - sql:
            sql: |
              FIX_ME

      # rollback #
      rollback:
        - dropTable:
            tableName: school_${componentNameAsSnake}
EOF
  fi
}

function _prepare_year_month_changelog_file() {
  local migrationYearMonthPath="$1"
  local yearMonthChangelogFileName="$2"
  local createTableFileName="$3"
  local isYearMonthChangelogFileExists="$4"

  _create_changelog_file "${migrationYearMonthPath}" "${yearMonthChangelogFileName}" "${isYearMonthChangelogFileExists}"
  _fill_changelog_file "${migrationYearMonthPath}" "${yearMonthChangelogFileName}" "${createTableFileName}"
}

function _prepare_year_changelog_file() {
  local migrationYearPath="$1"
  local yearChangelogFileName="$2"
  local isYearChangelogFileExists="$3"
  local yearMonthChangelogFileName="$4"
  local isYearMonthChangelogFileExists="$5"

  _create_changelog_file "${migrationYearPath}" "${yearChangelogFileName}" "${isYearChangelogFileExists}"

  if [ "${isYearMonthChangelogFileExists}" == "false" ]; then
    local month
    month=$(get_current_month)

    local fullIncludedYearMonthChangelogFileName
    fullIncludedYearMonthChangelogFileName="${month}/${yearMonthChangelogFileName}"

    _fill_changelog_file "${migrationYearPath}" "${yearChangelogFileName}" "${fullIncludedYearMonthChangelogFileName}"
  fi
}

function _prepare_root_changelog_file() {
  local migrationPath="$1"
  local yearChangelogFileName="$2"
  local isYearChangelogFileExists="$3"

  if [ "${isYearChangelogFileExists}" == "false" ]; then
    local year
    year=$(get_current_year)

    local fullIncludedYearChangelogFileName
    fullIncludedYearChangelogFileName="${year}/${yearChangelogFileName}"

    _fill_changelog_file "${migrationPath}" "changelog" "${fullIncludedYearChangelogFileName}"
  fi
}

function _get_create_file_name() {
  local componentName="$1"
  local nextFileNum="$2"

  local yearMonthDayPrefix
  yearMonthDayPrefix=$(get_year_month_day_prefix)

  local createTableFileName="${yearMonthDayPrefix}_${nextFileNum}_create${componentName}Table"

  echo "${createTableFileName}"
}

function _create_changelog_file() {
  local path="$1"
  local fileName="$2"
  local isFileExists="$3"

  if [ "${isFileExists}" == "false" ]; then
    cat > "${path}/${fileName}.yml" <<EOF
databaseChangeLog:
EOF
  fi
}

function _fill_changelog_file() {
  local path="$1"
  local fileName="$2"
  local includedFileName="$3"

  local fullPath="${path}/${fileName}.yml"

  if grep -q "file: ${includedFileName}.yml" "${fullPath}" 2>/dev/null; then
    return 0
  fi

  # If the file is not empty (-s) and the last character is not a newline (wc -l will return 0) — add one
  if [ -s "${fullPath}" ] && [ "$(tail -c 1 "${fullPath}" | wc -l)" -eq 0 ]; then
      echo "" >> "${fullPath}"
  fi

  cat >> "${fullPath}" <<EOF
  - include:
      relativeToChangelogFile: true
      file: ${includedFileName}.yml
EOF
}

function _get_year_month_changelog_file_name() {
  local yearMonthPrefix
  yearMonthPrefix=$(get_year_month_prefix)

  echo "${yearMonthPrefix}_changelog"
}

function _get_year_changelog_file_name() {
  local year
  year=$(get_current_year)

  echo "${year}_changelog"
}

function _get_next_file_num() {
  local path="$1"

  local yearMonthDayPrefix
  yearMonthDayPrefix=$(get_year_month_day_prefix)

  local lastFileNum
  lastFileNum=$(ls "${path}" 2>/dev/null \
    | grep "^${yearMonthDayPrefix}_[0-9]\+_" \
    | sed -E "s/^${yearMonthDayPrefix}_([0-9]+)_.*/\1/" \
    | sort -n \
    | tail -1
  )

  local nextFileNum=$(( ${lastFileNum:-0} + 1 ))

  echo "${nextFileNum}"
}
