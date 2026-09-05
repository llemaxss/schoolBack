package com.gmail.llemaxiss.app.common.hibernateFilter.component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Servlet filter that automatically enables Hibernate filters for every HTTP request
 *
 * <p>
 * By default, it enables the soft-delete and active-user filters to ensure that
 * only non-deleted and active users are returned in standard queries.
 * The filters are disabled after the request completes to keep the session clean.
 * </p>
 */
@RequiredArgsConstructor
@Component
public class HibernateFilterActivator extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(HibernateFilterActivator.class);

  private final HibernateFilterManager hibernateFilterManager;

  @SuppressWarnings("NullableProblems")
  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    hibernateFilterManager.enableNotDeletedOnlyFilter();
    hibernateFilterManager.enableActiveUserOnlyFilter();

    LOGGER.debug("Hibernate filters enabled for request: {}", request.getRequestURI());

    try {
      filterChain.doFilter(request, response);
    } finally {
      hibernateFilterManager.disableSoftDeleteFilter();
      hibernateFilterManager.disableActiveUserFilter();

      LOGGER.debug("Hibernate filters disabled after request: {}", request.getRequestURI());
    }
  }
}
