package com.gmail.llemaxiss.app._common.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public abstract class CommonService {
  
  @PersistenceContext
  protected EntityManager em;
  
}
