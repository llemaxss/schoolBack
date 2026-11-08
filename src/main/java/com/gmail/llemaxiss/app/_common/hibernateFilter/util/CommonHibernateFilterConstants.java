package com.gmail.llemaxiss.app._common.hibernateFilter.util;

/**
 * Constants for Hibernate filters used across the application
 *
 * <p>
 * Centralizes filter names, parameter names, and conditions.
 * </p>
 */
public interface CommonHibernateFilterConstants {

  String SOFT_DELETE_FILTER_NAME = "SOFT_DELETE_FILTER_NAME";

  String SOFT_DELETE_FILTER_PARAM_NAME = "isDeleted";

  String SOFT_DELETE_FILTER_CONDITION =
    //language=SQL
    """
    (
      (
        :isDeleted = true
        AND delete_ts IS NOT NULL
      )
      OR
      (
        :isDeleted = false
        AND delete_ts IS NULL
      )
    )
    
    """;

}
