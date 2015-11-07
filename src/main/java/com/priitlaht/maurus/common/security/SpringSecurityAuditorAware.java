package com.priitlaht.maurus.common.security;

import com.priitlaht.maurus.common.ApplicationConstants;
import com.priitlaht.maurus.common.util.security.SecurityUtil;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

  @Override
  public String getCurrentAuditor() {
    String userName = SecurityUtil.getCurrentUserLogin();
    return (userName != null ? userName : ApplicationConstants.SYSTEM_ACCOUNT);
  }
}
