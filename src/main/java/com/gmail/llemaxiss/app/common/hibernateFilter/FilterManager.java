package com.gmail.llemaxiss.app.common.hibernateFilter;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

/**
 * Utility for managing Hibernate filters programmatically
 *
 * <p>
 * Provides methods to enable or disable filters
 * using constants defined in {@link FilterConstants}.
 * </p>
 */
@RequiredArgsConstructor
@Component
public class FilterManager {

  private final EntityManager entityManager;

  /**
   * Enables the {@link FilterConstants#SOFT_DELETE_FILTER_NAME} filter to show only deleted entities
   *
   * <p>
   * Sets the {@link FilterConstants#SOFT_DELETE_FILTER_PARAM_NAME} parameter to {@code true}.
   * </p>
   */
  public void enableDeletedOnlyFilter() {
    getHibernateSession()
      .enableFilter(FilterConstants.SOFT_DELETE_FILTER_NAME)
      .setParameter(FilterConstants.SOFT_DELETE_FILTER_PARAM_NAME, true);
  }

  /**
   * Enables the {@link FilterConstants#SOFT_DELETE_FILTER_NAME} filter to show only not deleted entities
   *
   * <p>
   * Sets the {@link FilterConstants#SOFT_DELETE_FILTER_PARAM_NAME} parameter to {@code false}.
   * </p>
   */
  public void enableNotDeletedOnlyFilter() {
    getHibernateSession()
      .enableFilter(FilterConstants.SOFT_DELETE_FILTER_NAME)
      .setParameter(FilterConstants.SOFT_DELETE_FILTER_PARAM_NAME, false);
  }

  /**
   * Disables the {@link FilterConstants#SOFT_DELETE_FILTER_NAME} filter to show ALL entities (both deleted and not deleted)
   */
  public void disableSoftDeleteFilter() {
    getHibernateSession()
      .disableFilter(FilterConstants.SOFT_DELETE_FILTER_NAME);
  }

  /**
   * Enables the {@link FilterConstants#USER_ACTIVE_FILTER_NAME} filter to show only active users
   *
   * <p>
   * Sets the {@link FilterConstants#USER_ACTIVE_FILTER_PARAM_NAME} parameter to {@code true}.
   * </p>
   */
  public void enableActiveUserOnlyFilter() {
    getHibernateSession()
      .enableFilter(FilterConstants.USER_ACTIVE_FILTER_NAME)
      .setParameter(FilterConstants.USER_ACTIVE_FILTER_PARAM_NAME, true);
  }

  /**
   * Enables the {@link FilterConstants#USER_ACTIVE_FILTER_NAME} filter to show only not active users
   *
   * <p>
   * Sets the {@link FilterConstants#USER_ACTIVE_FILTER_PARAM_NAME} parameter to {@code false}.
   * </p>
   */
  public void enableNotActiveUserOnlyFilter() {
    getHibernateSession()
      .enableFilter(FilterConstants.USER_ACTIVE_FILTER_NAME)
      .setParameter(FilterConstants.USER_ACTIVE_FILTER_PARAM_NAME, false);
  }

  /**
   * Disables the{@link FilterConstants#USER_ACTIVE_FILTER_NAME} filter to show ALL users (both active and inactive)
   */
  public void disableActiveUserFilter() {
    getHibernateSession()
      .disableFilter(FilterConstants.USER_ACTIVE_FILTER_NAME);
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
