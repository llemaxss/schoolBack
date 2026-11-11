package com.gmail.llemaxiss.app._common.hibernateFilter.util.softDelete;


/**
 * Constants for Hibernate filters used across the application
 *
 * <p>
 * Centralizes filter names, parameter names, and conditions.
 * </p>
 */
public interface SoftDeletedHibernateFilterConstants {

  String FILTER_NAME = "SOFT_DELETE";

  String FILTER_PARAM_NAME = "isDeleted";

  String FILTER_CONDITION =
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
