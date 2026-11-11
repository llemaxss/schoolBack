@FilterDefs({
  @FilterDef(
    name = ActivityFilterConstants.FILTER_NAME,
    parameters = @ParamDef(
      type = Boolean.class,
      name = ActivityFilterConstants.FILTER_PARAM_NAME,
      resolver = UserActivityHibernateFilterParamResolver.class
    ),
    defaultCondition = ActivityFilterConstants.FILTER_CONDITION,
    autoEnabled = true
  )
})
package com.gmail.llemaxiss.app.user.hibernateFilter;

import com.gmail.llemaxiss.app.user.hibernateFilter.util.activity.ActivityFilterConstants;
import com.gmail.llemaxiss.app.user.hibernateFilter.util.activity.UserActivityHibernateFilterParamResolver;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;