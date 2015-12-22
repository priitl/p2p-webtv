package com.priitlaht.ppwebtv.security;

import com.priitlaht.ppwebtv.common.AuthoritiesConstants;
import com.priitlaht.ppwebtv.common.util.security.SecurityUtil;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the SecurityUtil utility class.
 *
 * @see SecurityUtil
 */
public class SecurityUtilTest {

  @Test
  public void testgetCurrentUserLogin() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
    SecurityContextHolder.setContext(securityContext);
    String login = SecurityUtil.getCurrentUserLogin();
    assertThat(login).isEqualTo("admin");
  }

  @Test
  public void testIsAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
    SecurityContextHolder.setContext(securityContext);
    boolean isAuthenticated = SecurityUtil.isAuthenticated();
    assertThat(isAuthenticated).isTrue();
  }

  @Test
  public void testAnonymousIsNotAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities));
    SecurityContextHolder.setContext(securityContext);
    boolean isAuthenticated = SecurityUtil.isAuthenticated();
    assertThat(isAuthenticated).isFalse();
  }
}
