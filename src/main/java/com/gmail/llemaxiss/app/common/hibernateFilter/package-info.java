@org.hibernate.annotations.FilterDefs({
  @org.hibernate.annotations.FilterDef(
    name = FilterConstants.SOFT_DELETE_FILTER_NAME,
    parameters = @org.hibernate.annotations.ParamDef(name = "isDeleted", type = Boolean.class),
    defaultCondition = FilterConstants.SOFT_DELETE_FILTER_CONDITION
  ),
  @org.hibernate.annotations.FilterDef(
    name = FilterConstants.USER_ACTIVE_FILTER_NAME,
    parameters = @org.hibernate.annotations.ParamDef(name = "isActive", type = Boolean.class),
    defaultCondition = FilterConstants.USER_ACTIVE_FILTER_CONDITION
  )
})
package com.gmail.llemaxiss.app.common.hibernateFilter;
