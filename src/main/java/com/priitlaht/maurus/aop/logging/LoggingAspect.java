package com.priitlaht.maurus.aop.logging;

import com.priitlaht.maurus.config.Constants;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.env.Environment;

import java.util.Arrays;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

/**
 * Aspect for logging execution of service and repository Spring components.
 */
@Slf4j
@Aspect
public class LoggingAspect {

  @Inject
  private Environment environment;

  @Pointcut("within(com.priitlaht.maurus.repository..*) || within(com.priitlaht.maurus.service..*) || within(com.priitlaht.maurus.web.rest..*)")
  public void loggingPointcut() {
  }

  @AfterThrowing(pointcut = "loggingPointcut()", throwing = "e")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
    Signature signature = joinPoint.getSignature();
    if (environment.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
      log.error("Exception in {}.{}() with cause = {}", signature.getDeclaringTypeName(), signature.getName(), e.getCause(), e);
    } else {
      log.error("Exception in {}.{}() with cause = {}", signature.getDeclaringTypeName(), signature.getName(), e.getCause());
    }
  }

  @Around("loggingPointcut()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    Signature signature = joinPoint.getSignature();
    if (log.isDebugEnabled()) {
      log.debug("Enter: {}.{}() with argument[s] = {}", signature.getDeclaringTypeName(), signature.getName(), Arrays.toString(joinPoint.getArgs()));
    }
    try {
      Object result = joinPoint.proceed();
      if (log.isDebugEnabled()) {
        log.debug("Exit: {}.{}() with result = {}", signature.getDeclaringTypeName(), signature.getName(), result);
      }
      return result;
    } catch (IllegalArgumentException e) {
      log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()), signature.getDeclaringTypeName(), signature.getName());
      throw e;
    }
  }
}
