package com.gmail.llemaxiss.app._common.security.config;

import com.gmail.llemaxiss.app._common.security.annotation.RequirePermission;
import com.gmail.llemaxiss.app._common.security.annotation.RequireRoleType;
import com.gmail.llemaxiss.app._common.security.component.AccessChecker;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.lang.annotation.Annotation;

@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {
  
  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  static Advisor requirePermissionAdvisor(ObjectProvider<AccessChecker> accessCheckerProvider) {
    AuthorizationManager<MethodInvocation> manager = (authentication, invocation) -> {
      RequirePermission annotation = AnnotatedElementUtils.findMergedAnnotation(
        invocation.getMethod(),
        RequirePermission.class
      );
      
      boolean isGranted = accessCheckerProvider.getObject()
        .hasAnyPermission(annotation.value());
      
      return new AuthorizationDecision(isGranted);
    };
    
    return createAdvisor(RequirePermission.class, manager);
  }
  
  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  static Advisor requireRoleTypeAdvisor(ObjectProvider<AccessChecker> accessCheckerProvider) {
    AuthorizationManager<MethodInvocation> manager = (authentication, invocation) -> {
      RequireRoleType annotation = AnnotatedElementUtils.findMergedAnnotation(
        invocation.getMethod(),
        RequireRoleType.class
      );
      
      boolean isGranted = accessCheckerProvider.getObject()
        .hasAnyRoleType(annotation.value());
      
      return new AuthorizationDecision(isGranted);
    };
    
    return createAdvisor(RequireRoleType.class, manager);
  }
  
  private static Advisor createAdvisor(Class<? extends Annotation> annotationType,
                                       AuthorizationManager<MethodInvocation> manager) {
    AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, annotationType, true);
    
    return new AuthorizationManagerBeforeMethodInterceptor(pointcut, manager);
  }
  
}
