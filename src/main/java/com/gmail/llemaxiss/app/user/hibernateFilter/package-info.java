@FilterDefs({
  @FilterDef(
    name = UserHibernateFilterConstants.USER_ACTIVE_FILTER_NAME,
    parameters = @ParamDef(
      name = UserHibernateFilterConstants.USER_ACTIVE_FILTER_PARAM_NAME,
      type = Boolean.class
    ),
    defaultCondition = UserHibernateFilterConstants.USER_ACTIVE_FILTER_CONDITION
  )
})
package com.gmail.llemaxiss.app.user.hibernateFilter;

import com.gmail.llemaxiss.app.user.hibernateFilter.util.UserHibernateFilterConstants;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;