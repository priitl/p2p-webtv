package com.priitlaht.ppwebtv.frontend;

import com.priitlaht.ppwebtv.backend.service.SocialService;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/social")
public class SocialResource {

  @Inject
  private SocialService socialService;
  @Inject
  private ProviderSignInUtils providerSignInUtils;

  @RequestMapping(value = "/signup", method = RequestMethod.GET)
  public RedirectView signUp(WebRequest webRequest, @CookieValue("NG_TRANSLATE_LANG_KEY") String langKey) {
    try {
      Connection<?> connection = providerSignInUtils.getConnectionFromSession(webRequest);
      socialService.createSocialUser(connection, langKey.replace("\"", ""));
      return new RedirectView(URIBuilder.fromUri("/#/social-register/" + connection.getKey().getProviderId())
        .queryParam("success", "true")
        .build().toString(), true);
    } catch (Exception e) {
      log.error("Exception creating social user: ", e);
      return new RedirectView(URIBuilder.fromUri("/#/social-register/no-provider")
        .queryParam("success", "false")
        .build().toString(), true);
    }
  }
}
