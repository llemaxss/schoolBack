package com.gmail.llemaxiss.app.common.hibernateFilter.config;

import com.gmail.llemaxiss.app.common.hibernateFilter.component.HibernateFilterInterceptor;
import lombok.AllArgsConstructor;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Hibernate filter settings.
 */
@AllArgsConstructor
@Configuration
public class HibernateFilterConfig {
  
  private final HibernateFilterInterceptor hibernateFilterInterceptor;
  
  /**
   * Registers the custom Hibernate interceptor to enable filters globally
   * for every transactional session
   *
   * @return {@link HibernatePropertiesCustomizer} bean
   */
  @Bean
  public HibernatePropertiesCustomizer hibernateCustomizer() {
    return properties -> properties.put(
      AvailableSettings.INTERCEPTOR,
      hibernateFilterInterceptor
    );
  }
}
