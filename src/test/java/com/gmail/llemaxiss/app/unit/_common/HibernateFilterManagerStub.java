package com.gmail.llemaxiss.app.unit._common;

import com.gmail.llemaxiss.app._common.hibernateFilter.component.HibernateFilterManager;
import jakarta.validation.constraints.NotNull;

import java.util.function.Supplier;

/**
 * Replacement of {@link HibernateFilterManager} for unit tests
 *
 * <p>
 * There is no Hibernate session in unit tests, so the filters are not changed,
 * the action is just executed.
 * </p>
 */
public class HibernateFilterManagerStub extends HibernateFilterManager {
  
  public HibernateFilterManagerStub() {
    super(null);
  }
  
  @Override
  public <T> T executeWithSoftDeletedOnlyFilter(Supplier<T> action) {
    return action.get();
  }
  
  @Override
  public <T> T executeWithNotSoftDeletedOnlyFilter(Supplier<T> action) {
    return action.get();
  }
  
  @Override
  public <T> T executeWithoutSoftDeleteFilter(@NotNull Supplier<T> action) {
    return action.get();
  }
  
  @Override
  public <T> T executeWithActiveUserOnlyFilter(@NotNull Supplier<T> action) {
    return action.get();
  }
  
  @Override
  public <T> T executeWithNotActiveUserOnlyFilter(@NotNull Supplier<T> action) {
    return action.get();
  }
  
  @Override
  public <T> T executeWithoutActiveUserFilter(@NotNull Supplier<T> action) {
    return action.get();
  }
  
}