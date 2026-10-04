package com.gmail.llemaxiss.app.unit._common;

import com.gmail.llemaxiss.app._common.service.CommonService;
import jakarta.persistence.EntityManager;
import org.mockito.Mock;

import java.lang.reflect.Field;

public abstract class CommonServiceImplTest {
  
  protected static final String ERROR_CODE_FIELD = "errorCode";
  
  @Mock
  protected EntityManager em;
  
  public <S extends CommonService> void setUpEntityManager(S... services) throws Exception {
    Field emField = CommonService.class.getDeclaredField("em");
    
    emField.setAccessible(true);
    
    for (S service : services) {
      emField.set(service, em);
    }
  }
}
