@org.hibernate.annotations.FilterDefs({
  @org.hibernate.annotations.FilterDef(
    name = HibernateFilterConstants.SOFT_DELETE_FILTER_NAME,
    parameters = @org.hibernate.annotations.ParamDef(
      name = HibernateFilterConstants.SOFT_DELETE_FILTER_PARAM_NAME,
      type = Boolean.class
    ),
    defaultCondition = HibernateFilterConstants.SOFT_DELETE_FILTER_CONDITION
  ),
  @org.hibernate.annotations.FilterDef(
    name = HibernateFilterConstants.USER_ACTIVE_FILTER_NAME,
    parameters = @org.hibernate.annotations.ParamDef(
      name = HibernateFilterConstants.USER_ACTIVE_FILTER_PARAM_NAME,
      type = Boolean.class
    ),
    defaultCondition = HibernateFilterConstants.USER_ACTIVE_FILTER_CONDITION
  )
})
package com.gmail.llemaxiss.app.common.hibernateFilter;

import com.gmail.llemaxiss.app.common.hibernateFilter.util.HibernateFilterConstants;