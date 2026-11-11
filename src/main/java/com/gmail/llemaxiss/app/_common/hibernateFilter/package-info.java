@FilterDefs({
  @FilterDef(
    name = SoftDeletedHibernateFilterConstants.FILTER_NAME,
    parameters = @ParamDef(
      type = Boolean.class,
      name = SoftDeletedHibernateFilterConstants.FILTER_PARAM_NAME,
      resolver = SoftDeletedHibernateFilterParamResolver.class
    ),
    defaultCondition = SoftDeletedHibernateFilterConstants.FILTER_CONDITION,
    autoEnabled = true
  )
})
package com.gmail.llemaxiss.app._common.hibernateFilter;

import com.gmail.llemaxiss.app._common.hibernateFilter.util.softDelete.SoftDeletedHibernateFilterConstants;
import com.gmail.llemaxiss.app._common.hibernateFilter.util.softDelete.SoftDeletedHibernateFilterParamResolver;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;