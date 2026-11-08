package com.gmail.llemaxiss.app.user.hibernateFilter.util;

public interface UserHibernateFilterConstants {
  
  String USER_ACTIVE_FILTER_NAME = "USER_ACTIVE_FILTER_NAME";
  
  String USER_ACTIVE_FILTER_PARAM_NAME = "isActive";
  
  String USER_ACTIVE_FILTER_CONDITION =
    //language=SQL
    """
    (
      (
        :isActive = true
        AND is_active = true
      )
      OR
      (
        :isActive = false
        AND is_active = false
      )
    )
    """;
  
}
