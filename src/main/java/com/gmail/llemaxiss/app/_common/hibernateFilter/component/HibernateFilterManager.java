package com.gmail.llemaxiss.app._common.hibernateFilter.component;

import com.gmail.llemaxiss.app._common.hibernateFilter.config.HibernateFilterConfig;
import com.gmail.llemaxiss.app._common.hibernateFilter.util.CommonHibernateFilterConstants;
import com.gmail.llemaxiss.app.user.hibernateFilter.util.UserHibernateFilterConstants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Utility for managing Hibernate filters programmatically
 *
 * <p>
 * Provides methods to enable or disable filters
 * using constants defined in {@link CommonHibernateFilterConstants}.
 * </p>
 */
@RequiredArgsConstructor
@Component
public class HibernateFilterManager {
  
  /**
   * ObjectProvider for lazy resolution of EntityManager
   *
   * <p>
   * Using ObjectProvider instead of direct EntityManager injection
   * breaks the circular dependency chain:
   * {@link HibernateFilterConfig} ->
   * {@link HibernateFilterInterceptor} ->
   * {@link HibernateFilterManager} ->
   * {@link EntityManager} ->
   * {@link EntityManagerFactory} ->
   * {@link HibernateFilterConfig} (from 1 step)
   * </p>
   */
  private final ObjectProvider<EntityManager> entityManagerProvider;
  
  
  /**
   * Enables the {@link CommonHibernateFilterConstants#SOFT_DELETE_FILTER_NAME} filter to show only deleted entities
   *
   * <p>
   * Sets the {@link CommonHibernateFilterConstants#SOFT_DELETE_FILTER_PARAM_NAME} parameter to {@code true}.
   * </p>
   */
  public void enableDeletedOnlyFilter() {
    enableFilter(
      CommonHibernateFilterConstants.SOFT_DELETE_FILTER_NAME,
      Map.of(CommonHibernateFilterConstants.SOFT_DELETE_FILTER_PARAM_NAME, true)
    );
  }

  /**
   * Enables the {@link CommonHibernateFilterConstants#SOFT_DELETE_FILTER_NAME} filter to show only not deleted entities
   *
   * <p>
   * Sets the {@link CommonHibernateFilterConstants#SOFT_DELETE_FILTER_PARAM_NAME} parameter to {@code false}.
   * </p>
   */
  public void enableNotDeletedOnlyFilter() {
    enableFilter(
      CommonHibernateFilterConstants.SOFT_DELETE_FILTER_NAME,
      Map.of(CommonHibernateFilterConstants.SOFT_DELETE_FILTER_PARAM_NAME, false)
    );
  }

  /**
   * Disables the {@link CommonHibernateFilterConstants#SOFT_DELETE_FILTER_NAME} filter to show ALL entities (both deleted and not deleted)
   */
  public void disableSoftDeleteFilter() {
    disableFilter(CommonHibernateFilterConstants.SOFT_DELETE_FILTER_NAME);
  }

  /**
   * Enables the {@link UserHibernateFilterConstants#USER_ACTIVE_FILTER_NAME} filter to show only active users
   *
   * <p>
   * Sets the {@link UserHibernateFilterConstants#USER_ACTIVE_FILTER_PARAM_NAME} parameter to {@code true}.
   * </p>
   */
  public void enableActiveUserOnlyFilter() {
    enableFilter(
      UserHibernateFilterConstants.USER_ACTIVE_FILTER_NAME,
      Map.of(UserHibernateFilterConstants.USER_ACTIVE_FILTER_PARAM_NAME, true)
    );
  }

  /**
   * Enables the {@link UserHibernateFilterConstants#USER_ACTIVE_FILTER_NAME} filter to show only not active users
   *
   * <p>
   * Sets the {@link UserHibernateFilterConstants#USER_ACTIVE_FILTER_PARAM_NAME} parameter to {@code false}.
   * </p>
   */
  public void enableNotActiveUserOnlyFilter() {
    enableFilter(
      UserHibernateFilterConstants.USER_ACTIVE_FILTER_NAME,
      Map.of(UserHibernateFilterConstants.USER_ACTIVE_FILTER_PARAM_NAME, false)
    );
  }

  /**
   * Disables the{@link UserHibernateFilterConstants#USER_ACTIVE_FILTER_NAME} filter to show ALL users (both active and inactive)
   */
  public void disableActiveUserFilter() {
    disableFilter(UserHibernateFilterConstants.USER_ACTIVE_FILTER_NAME);
  }
  
  public void enableFilter(@NotBlank String filterName) {
    enableFilter(filterName, null);
  }

  public void enableFilter(@NotBlank String filterName, Map<String, Object> filterParameters) {
    Session session = getHibernateSession();
    
    Filter filter = session.enableFilter(filterName);
    
    if (MapUtils.isNotEmpty(filterParameters)) {
      filterParameters.forEach(filter::setParameter);
    }
  }
  
  public void disableFilter(@NotBlank String filterName) {
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
    return entityManagerProvider
      .getObject()
      .unwrap(Session.class);
  }

}
