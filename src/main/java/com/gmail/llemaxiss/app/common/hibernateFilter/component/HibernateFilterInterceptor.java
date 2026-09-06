package com.gmail.llemaxiss.app.common.hibernateFilter.component;

import lombok.AllArgsConstructor;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Hibernate interceptor that automatically enables soft-delete and active-user filters
 * at the beginning of every transaction
 */
@AllArgsConstructor
@Component
public class HibernateFilterInterceptor implements Interceptor {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(HibernateFilterInterceptor.class);
  
  private final HibernateFilterManager hibernateFilterManager;
  
  @Override
  public void afterTransactionBegin(Transaction tx) {
    hibernateFilterManager.enableNotDeletedOnlyFilter();
    hibernateFilterManager.enableActiveUserOnlyFilter();
    
    LOGGER.debug("Hibernate filters enabled for transaction");
  }
}
