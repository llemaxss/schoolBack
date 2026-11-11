package com.gmail.llemaxiss.app.user.hibernateFilter.util.activity;

public interface ActivityFilterConstants {
  
  String FILTER_NAME = "USER_ACTIVITY";
  
  String FILTER_PARAM_NAME = "isActive";
  
  String FILTER_CONDITION =
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
