package com.gmail.llemaxiss.app.common.hibernateFilter;


import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Utility for managing Hibernate filters programmatically
 *
 * <p>
 * Provides methods to enable or disable filters
 * using constants defined in {@link HibernateFilterConstants}.
 * </p>
 */
@RequiredArgsConstructor
@Component
public class HibernateFilterManager {

  private final EntityManager entityManager;

  /**
   * Enables the {@link HibernateFilterConstants#SOFT_DELETE_FILTER_NAME} filter to show only deleted entities
   *
   * <p>
   * Sets the {@link HibernateFilterConstants#SOFT_DELETE_FILTER_PARAM_NAME} parameter to {@code true}.
   * </p>
   */
  public void enableDeletedOnlyFilter() {
    enableFilter(
      HibernateFilterConstants.SOFT_DELETE_FILTER_NAME,
      Map.of(HibernateFilterConstants.SOFT_DELETE_FILTER_PARAM_NAME, true)
    );
  }

  /**
   * Enables the {@link HibernateFilterConstants#SOFT_DELETE_FILTER_NAME} filter to show only not deleted entities
   *
   * <p>
   * Sets the {@link HibernateFilterConstants#SOFT_DELETE_FILTER_PARAM_NAME} parameter to {@code false}.
   * </p>
   */
  public void enableNotDeletedOnlyFilter() {
    enableFilter(
      HibernateFilterConstants.SOFT_DELETE_FILTER_NAME,
      Map.of(HibernateFilterConstants.SOFT_DELETE_FILTER_PARAM_NAME, false)
    );
  }

  /**
   * Disables the {@link HibernateFilterConstants#SOFT_DELETE_FILTER_NAME} filter to show ALL entities (both deleted and not deleted)
   */
  public void disableSoftDeleteFilter() {
    disableFilter(HibernateFilterConstants.SOFT_DELETE_FILTER_NAME);
  }

  /**
   * Enables the {@link HibernateFilterConstants#USER_ACTIVE_FILTER_NAME} filter to show only active users
   *
   * <p>
   * Sets the {@link HibernateFilterConstants#USER_ACTIVE_FILTER_PARAM_NAME} parameter to {@code true}.
   * </p>
   */
  public void enableActiveUserOnlyFilter() {
    enableFilter(
      HibernateFilterConstants.USER_ACTIVE_FILTER_NAME,
      Map.of(HibernateFilterConstants.USER_ACTIVE_FILTER_NAME, true)
    );
  }

  /**
   * Enables the {@link HibernateFilterConstants#USER_ACTIVE_FILTER_NAME} filter to show only not active users
   *
   * <p>
   * Sets the {@link HibernateFilterConstants#USER_ACTIVE_FILTER_PARAM_NAME} parameter to {@code false}.
   * </p>
   */
  public void enableNotActiveUserOnlyFilter() {
    enableFilter(
      HibernateFilterConstants.USER_ACTIVE_FILTER_NAME,
      Map.of(HibernateFilterConstants.USER_ACTIVE_FILTER_PARAM_NAME, false)
    );
  }

  /**
   * Disables the{@link HibernateFilterConstants#USER_ACTIVE_FILTER_NAME} filter to show ALL users (both active and inactive)
   */
  public void disableActiveUserFilter() {
    disableFilter(HibernateFilterConstants.USER_ACTIVE_FILTER_NAME);
  }
  
  public void enableFilter(@NotNull String filterName) {
    enableFilter(filterName, null);
  }

  public void enableFilter(@NotNull String filterName, Map<String, Object> filterParameters) {
    Session session = getHibernateSession();
    
    Filter filter = session.enableFilter(filterName);
    
    if (MapUtils.isNotEmpty(filterParameters)) {
      filterParameters.forEach(filter::setParameter);
    }
  }
  
  public void disableFilter(@NotNull String filterName) {
    getHibernateSession()
      .disableFilter(filterName);
  }

  /**
   * Retrieves the current Hibernate {@link Session} from the {@link EntityManager}
   *
   * @return the current Hibernate {@link Session}
   */
  private Session getHibernateSession() {
    return entityManager.unwrap(Session.class);
  }

}
