package com.priitlaht.ppwebtv.common.security;

import com.priitlaht.ppwebtv.common.ApplicationConstants;
import com.priitlaht.ppwebtv.common.util.security.SecurityUtil;

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
