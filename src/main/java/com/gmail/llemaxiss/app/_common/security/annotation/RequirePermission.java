package com.gmail.llemaxiss.app._common.security.annotation;

import com.gmail.llemaxiss.app.permission.enums.Permission;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Access is granted if the current user has at least one of the specified {@link Permission}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
  
  Permission[] value();
  
}
