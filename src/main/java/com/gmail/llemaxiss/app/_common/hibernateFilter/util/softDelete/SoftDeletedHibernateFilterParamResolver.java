package com.gmail.llemaxiss.app._common.hibernateFilter.util.softDelete;

import java.util.function.Supplier;

public class SoftDeletedHibernateFilterParamResolver implements Supplier<Boolean> {
  
  @Override
  public Boolean get() {
    return false;
  }
  
}
