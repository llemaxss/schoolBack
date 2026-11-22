#!/bin/bash

function get_current_year() {
  date +"%y"
}

function get_current_month() {
  date +"%m"
}

function get_current_day() {
  date +"%d"
}

function get_year_month_prefix() {
  local year
  year=$(get_current_year)

  local month
  month=$(get_current_month)

  local prefix
  prefix="${year}${month}"

  echo "${prefix}"
}

function get_year_month_day_prefix() {
  local year
  year=$(get_current_year)

  local month
  month=$(get_current_month)

  local day
  day=$(get_current_day)

  local fullDatePrefix
  fullDatePrefix="${year}${month}${day}"

  echo "${fullDatePrefix}"
}

function get_component_name_by_words() {
  local componentName="$1"
  local firstLetterCase="${2:-}"

  local result
  result=$(echo "${componentName}" | sed -E 's/([A-Z])/ \L\1/g' | sed 's/^ //')

  if [ "${firstLetterCase}" == "upper" ]; then
    echo "${result}" | sed 's/^\(.\)/\U\1/'
  else
    echo "${result}"
  fi
}

function get_component_name_as_snake() {
  local componentName="$1"

  echo "${componentName}" | sed -E 's/([A-Z])/_\L\1/g' | sed 's/^_//'
}

function lowercase_first_letter() {
  local text="$1"

  echo "${text}" | sed 's/^\(.\)/\L\1/'
}

function add_plural_suffix() {
  local text="$1"
  local last_char="${text: -1}"

  if [ "$last_char" == "s" ]; then
    echo "${text}es"
  else
    echo "${text}s"
  fi
}

function is_file_exists() {
  if [ -f "$1" ]; then
    echo "true"
  else
    echo "false"
  fi
}
