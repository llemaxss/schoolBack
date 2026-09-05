package com.gmail.llemaxiss.app.common.hibernateFilter;

/**
 * Constants for Hibernate filters used across the application
 *
 * <p>
 * Centralizes filter names, parameter names, and conditions.
 * </p>
 */
public interface HibernateFilterConstants {

  String SOFT_DELETE_FILTER_NAME = "SOFT_DELETE_FILTER_NAME";

  String SOFT_DELETE_FILTER_PARAM_NAME = "isDeleted";

  String SOFT_DELETE_FILTER_CONDITION =
    //language=SQL
    """
    (
      :isDeleted = true
      AND delete_ts IS NOT NULL
    )
    OR
    (
      :isDeleted = false
      AND delete_ts IS NULL
    )
    """;

  String USER_ACTIVE_FILTER_NAME = "USER_ACTIVE_FILTER_NAME";

  String USER_ACTIVE_FILTER_PARAM_NAME = "isActive";

  String USER_ACTIVE_FILTER_CONDITION =
    //language=SQL
    """
    (
      :isActive = true
      AND is_active = true
    )
    OR
    (
      :isActive = false
      AND is_active = false
    )
    """;

}
