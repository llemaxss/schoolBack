@FilterDefs({
  @FilterDef(
    name = CommonHibernateFilterConstants.SOFT_DELETE_FILTER_NAME,
    parameters = @ParamDef(
      name = CommonHibernateFilterConstants.SOFT_DELETE_FILTER_PARAM_NAME,
      type = Boolean.class
    ),
    defaultCondition = CommonHibernateFilterConstants.SOFT_DELETE_FILTER_CONDITION
  )
})
package com.gmail.llemaxiss.app._common.hibernateFilter;

import com.gmail.llemaxiss.app._common.hibernateFilter.util.CommonHibernateFilterConstants;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;