package com.gmail.llemaxiss.app.common.hibernateFilter.component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

/**
 * Hibernate interceptor that automatically enables soft-delete and active-user filters
 * at the beginning of every transaction
 */
@Slf4j
@AllArgsConstructor
@Component
public class HibernateFilterInterceptor implements Interceptor {
  
  private final HibernateFilterManager hibernateFilterManager;
  
  @Override
  public void afterTransactionBegin(Transaction tx) {
    hibernateFilterManager.enableNotDeletedOnlyFilter();
    hibernateFilterManager.enableActiveUserOnlyFilter();
    
    log.debug("Hibernate filters enabled for transaction");
  }
}
