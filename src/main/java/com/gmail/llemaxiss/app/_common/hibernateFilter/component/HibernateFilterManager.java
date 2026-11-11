package com.gmail.llemaxiss.app._common.hibernateFilter.component;

import com.gmail.llemaxiss.app._common.hibernateFilter.util.softDelete.SoftDeletedHibernateFilterConstants;
import com.gmail.llemaxiss.app.user.hibernateFilter.util.activity.ActivityFilterConstants;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.internal.FilterImpl;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Utility for changing Hibernate filters temporarily
 *
 * <p>
 * The filters are enabled by default by Hibernate itself (see {@code autoEnabled} in the filter definitions).
 * A filter can be changed only for the time of the given action: before the action it is switched
 * to the requested state, after the action (even if it throws an exception) it is returned
 * to the state it had before.
 * </p>
 */
@RequiredArgsConstructor
@Component
public class HibernateFilterManager {
  
  private final EntityManager entityManager;
  
  
  /**
   * Executes the action with the {@link SoftDeletedHibernateFilterConstants#FILTER_NAME} filter
   * which shows only deleted entities
   */
  public <T> T executeWithSoftDeletedOnlyFilter(@NotNull Supplier<T> action) {
    return executeWithFilterState(
      SoftDeletedHibernateFilterConstants.FILTER_NAME,
      SoftDeletedHibernateFilterConstants.FILTER_PARAM_NAME,
      FilterState.enabled(true),
      action
    );
  }
  
  /**
   * Executes the action with the {@link SoftDeletedHibernateFilterConstants#FILTER_NAME} filter
   * which shows only not deleted entities
   */
  public <T> T executeWithNotSoftDeletedOnlyFilter(@NotNull Supplier<T> action) {
    return executeWithFilterState(
      SoftDeletedHibernateFilterConstants.FILTER_NAME,
      SoftDeletedHibernateFilterConstants.FILTER_PARAM_NAME,
      FilterState.enabled(false),
      action
    );
  }
  
  /**
   * Executes the action without the {@link SoftDeletedHibernateFilterConstants#FILTER_NAME} filter
   * (both deleted and not deleted entities are visible)
   */
  public <T> T executeWithoutSoftDeleteFilter(@NotNull Supplier<T> action) {
    return executeWithFilterState(
      SoftDeletedHibernateFilterConstants.FILTER_NAME,
      SoftDeletedHibernateFilterConstants.FILTER_PARAM_NAME,
      FilterState.disabled(),
      action
    );
  }
  
  /**
   * Executes the action with the {@link ActivityFilterConstants#FILTER_NAME} filter
   * which shows only active users
   */
  public <T> T executeWithActiveUserOnlyFilter(@NotNull Supplier<T> action) {
    return executeWithFilterState(
      ActivityFilterConstants.FILTER_NAME,
      ActivityFilterConstants.FILTER_PARAM_NAME,
      FilterState.enabled(true),
      action
    );
  }
  
  /**
   * Executes the action with the {@link ActivityFilterConstants#FILTER_NAME} filter
   * which shows only not active users
   */
  public <T> T executeWithNotActiveUserOnlyFilter(@NotNull Supplier<T> action) {
    return executeWithFilterState(
      ActivityFilterConstants.FILTER_NAME,
      ActivityFilterConstants.FILTER_PARAM_NAME,
      FilterState.enabled(false),
      action
    );
  }
  
  /**
   * Executes the action without the {@link ActivityFilterConstants#FILTER_NAME} filter
   * (both active and inactive users are visible)
   */
  public <T> T executeWithoutActiveUserFilter(@NotNull Supplier<T> action) {
    return executeWithFilterState(
      ActivityFilterConstants.FILTER_NAME,
      ActivityFilterConstants.FILTER_PARAM_NAME,
      FilterState.disabled(),
      action
    );
  }
  
  private <T> T executeWithFilterState(@NotBlank String filterName, @NotBlank String paramName,
                                       @NotNull FilterState newState, @NotNull Supplier<T> action) {
    FilterState previousState = getFilterState(filterName, paramName);
    
    applyFilterState(filterName, paramName, newState);
    
    try {
      return action.get();
    } finally {
      applyFilterState(filterName, paramName, previousState);
    }
  }
  
  @NotNull
  private FilterState getFilterState(@NotBlank String filterName, @NotBlank String paramName) {
    Filter filter = getHibernateSession()
      .getEnabledFilter(filterName);
    
    if (filter == null) {
      return FilterState.disabled();
    }
    
    Optional<Object> valueOptional = findParameterValue(filter, paramName);
    
    return valueOptional.map(FilterState::enabled)
      .orElseGet(FilterState::enabledByDefault);
  }
  
  private void applyFilterState(@NotBlank String filterName, @NotBlank String paramName, @NotNull FilterState state) {
    if (!state.isEnabled()) {
      disableFilter(filterName);
      return;
    }
    
    if (state.getParameterValue() == null) {
      enableFilter(filterName, null);
      return;
    }
    
    enableFilter(
      filterName,
      Map.of(paramName, state.getParameterValue())
    );
  }
  
  /**
   * Takes the value which was explicitly set for the filter parameter
   *
   * <p>
   * Empty means that the value was not set and the default one (from the filter definition) is used.
   * Hibernate has no public API for it, so the internal {@link FilterImpl} is used.
   * </p>
   */
  @NotNull
  private Optional<Object> findParameterValue(@NotNull Filter filter, @NotBlank String paramName) {
    if (!(filter instanceof FilterImpl filterImpl)) {
      String message = String.format("Unexpected Hibernate filter class: %s", filter.getClass());
      throw new IllegalStateException(message);
    }
    
    return Optional.ofNullable(
      filterImpl.getParameter(paramName)
    );
  }
  
  private void enableFilter(@NotBlank String filterName, Map<String, Object> filterParameters) {
    Filter filter = getHibernateSession()
      .enableFilter(filterName);
    
    if (MapUtils.isNotEmpty(filterParameters)) {
      filterParameters.forEach(filter::setParameter);
    }
  }
  
  private void disableFilter(@NotBlank String filterName) {
    getHibernateSession()
      .disableFilter(filterName);
  }
  
  /**
   * Retrieves the current Hibernate {@link Session} from the {@link EntityManager}
   *
   * @return the current Hibernate {@link Session}
   */
  @NotNull
  private Session getHibernateSession() {
    return entityManager.unwrap(Session.class);
  }
  
  /**
   * State of a filter: disabled, enabled with the default parameter value ({@code parameterValue} is null)
   * or enabled with an explicit parameter value
   */
  @Getter
  @AllArgsConstructor
  private static class FilterState {
    
    private final boolean isEnabled;
    
    private final Object parameterValue;
    
    static FilterState disabled() {
      return new FilterState(false, null);
    }
    
    static FilterState enabledByDefault() {
      return new FilterState(true, null);
    }
    
    static FilterState enabled(@NotNull Object parameterValue) {
      return new FilterState(true, parameterValue);
    }
    
  }
  
}