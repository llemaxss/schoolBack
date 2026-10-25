package com.gmail.llemaxiss.app._common.security.annotation;

import com.gmail.llemaxiss.app.role.enums.RoleType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Access is granted if the current user has at least one of the specified {@link RoleType}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRoleType {
  
  RoleType[] value();
  
}
