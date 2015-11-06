package com.priitlaht.maurus.config.social;

import com.priitlaht.maurus.repository.CustomSocialUsersConnectionRepository;
import com.priitlaht.maurus.repository.SocialUserConnectionRepository;
import com.priitlaht.maurus.security.social.CustomSignInAdapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

/**
 * Basic Spring Social configuration. <p> <p>Creates the beans necessary to manage Connections to social services and link accounts from those
 * services to internal Users.</p>
 */
@Slf4j
@Configuration
@EnableSocial
public class SocialConfiguration implements SocialConfigurer {

  @Inject
  private SocialUserConnectionRepository socialUserConnectionRepository;

  @Override
  public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
    addConfiguration(configurer, environment, SocialProvider.GOOGLE);
    addConfiguration(configurer, environment, SocialProvider.FACEBOOK);
    addConfiguration(configurer, environment, SocialProvider.TWITTER);
  }

  private void addConfiguration(ConnectionFactoryConfigurer configurer, Environment environment, SocialProvider socialProvider) {
    String clientId = environment.getProperty(format("spring.social.%s.clientId", socialProvider.name().toLowerCase()));
    String clientSecret = environment.getProperty(format("spring.social.%s.clientSecret", socialProvider.name().toLowerCase()));
    if (clientId != null && clientSecret != null) {
      log.debug(format("Configuring %s ConnectionFactory", socialProvider));
      switch (socialProvider) {
        case GOOGLE:
          addGoogleConfiguration(configurer, clientId, clientSecret);
          break;
        case FACEBOOK:
          addFacebookConfiguration(configurer, clientId, clientSecret);
          break;
        case TWITTER:
          addTwitterConfiguration(configurer, clientId, clientSecret);
          break;
      }

    } else {
      log.error(format("Cannot configure %s ConnectionFactory : id or secret is null", socialProvider));
    }
  }

  private void addGoogleConfiguration(ConnectionFactoryConfigurer configurer, String clientId, String clientSecret) {
    GoogleConnectionFactory googleConnectionFactory = new GoogleConnectionFactory(clientId, clientSecret);
    configurer.addConnectionFactory(googleConnectionFactory);
  }

  private void addFacebookConfiguration(ConnectionFactoryConfigurer configurer, String clientId, String clientSecret) {
    FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(clientId, clientSecret);
    configurer.addConnectionFactory(facebookConnectionFactory);
  }

  private void addTwitterConfiguration(ConnectionFactoryConfigurer configurer, String clientId, String clientSecret) {
    TwitterConnectionFactory twitterConnectionFactory = new TwitterConnectionFactory(clientId, clientSecret);
    configurer.addConnectionFactory(twitterConnectionFactory);
  }

  @Override
  public UserIdSource getUserIdSource() {
    return new AuthenticationNameUserIdSource();
  }

  @Override
  public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator locator) {
    return new CustomSocialUsersConnectionRepository(socialUserConnectionRepository, locator);
  }

  @Bean
  public SignInAdapter signInAdapter() {
    return new CustomSignInAdapter();
  }

  @Bean
  public ProviderSignInController providerSignInController(ConnectionFactoryLocator locator, UsersConnectionRepository repository, SignInAdapter adapter) throws Exception {
    ProviderSignInController signInController = new ProviderSignInController(locator, repository, adapter);
    signInController.setSignUpUrl("/social/signup");
    return signInController;
  }

  @Bean
  public ProviderSignInUtils getProviderSignInUtils(ConnectionFactoryLocator locator, UsersConnectionRepository repository) {
    return new ProviderSignInUtils(locator, repository);
  }

  private enum SocialProvider {
    GOOGLE, FACEBOOK, TWITTER
  }
}
