package com.gmail.llemaxiss.app.user.hibernateFilter.util.activity;

import java.util.function.Supplier;

public class UserActivityHibernateFilterParamResolver implements Supplier<Boolean> {
  
  @Override
  public Boolean get() {
    return true;
  }
  
}
