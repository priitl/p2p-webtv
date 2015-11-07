package com.priitlaht.maurus.common.util.security;

import com.priitlaht.maurus.common.AuthoritiesConstants;
import com.priitlaht.maurus.common.security.user.CustomUserDetails;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class SecurityUtil {

  public static String getCurrentUserLogin() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    String userName = null;
    if (authentication != null) {
      if (authentication.getPrincipal() instanceof UserDetails) {
        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
        userName = springSecurityUser.getUsername();
      } else if (authentication.getPrincipal() instanceof String) {
        userName = (String) authentication.getPrincipal();
      }
    }
    return userName;
  }

  public static boolean isAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();
    if (authorities != null) {
      for (GrantedAuthority authority : authorities) {
        if (authority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)) {
          return false;
        }
      }
    }
    return true;
  }

  public static Long getCurrentUserId() {
    return getCurrentUser().getId();
  }

  public static CustomUserDetails getCurrentUser() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication != null) {
      if (authentication.getPrincipal() instanceof CustomUserDetails) {
        return (CustomUserDetails) authentication.getPrincipal();
      }
    }
    throw new IllegalStateException("User not found!");
  }

  public static boolean isUserInRole(String authority) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication != null) {
      if (authentication.getPrincipal() instanceof UserDetails) {
        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
        return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(authority));
      }
    }
    return false;
  }
}
