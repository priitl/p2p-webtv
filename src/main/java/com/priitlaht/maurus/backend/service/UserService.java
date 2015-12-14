package com.priitlaht.maurus.backend.service;

import com.priitlaht.maurus.backend.domain.Authority;
import com.priitlaht.maurus.backend.domain.User;
import com.priitlaht.maurus.backend.repository.AuthorityRepository;
import com.priitlaht.maurus.backend.repository.PersistentTokenRepository;
import com.priitlaht.maurus.backend.repository.UserRepository;
import com.priitlaht.maurus.backend.repository.search.UserSearchRepository;
import com.priitlaht.maurus.common.util.random.RandomUtil;
import com.priitlaht.maurus.common.util.security.SecurityUtil;
import com.priitlaht.maurus.frontend.user.ManagedUserDTO;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserService {

  @Inject
  private PasswordEncoder passwordEncoder;
  @Inject
  private UserRepository userRepository;
  @Inject
  private UserSearchRepository userSearchRepository;
  @Inject
  private PersistentTokenRepository persistentTokenRepository;
  @Inject
  private AuthorityRepository authorityRepository;

  public Optional<User> activateRegistration(String key) {
    log.debug("Activating user for activation key {}", key);
    userRepository.findOneByActivationKey(key)
      .map(user -> {
        user.setActivated(true);
        user.setActivationKey(null);
        userRepository.save(user);
        userSearchRepository.save(user);
        log.debug("Activated user: {}", user);
        return user;
      });
    return Optional.empty();
  }

  public Optional<User> completePasswordReset(String newPassword, String key) {
    log.debug("Reset user password for reset key {}", key);

    return userRepository.findOneByResetKey(key)
      .filter(user -> {
        ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
        return user.getResetDate().isAfter(oneDayAgo);
      })
      .map(user -> {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetKey(null);
        user.setResetDate(null);
        userRepository.save(user);
        return user;
      });
  }

  public Optional<User> requestPasswordReset(String mail) {
    return userRepository.findOneByEmail(mail)
      .filter(User::isActivated)
      .map(user -> {
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(ZonedDateTime.now());
        userRepository.save(user);
        return user;
      });
  }

  public User createUserInformation(String login, String password, String firstName, String lastName, String email,
                                    String langKey) {

    User newUser = new User();
    Authority authority = authorityRepository.findOne("ROLE_USER");
    Set<Authority> authorities = new HashSet<>();
    String encryptedPassword = passwordEncoder.encode(password);
    newUser.setLogin(login);
    newUser.setPassword(encryptedPassword);
    newUser.setFirstName(firstName);
    newUser.setLastName(lastName);
    newUser.setEmail(email);
    newUser.setLangKey(langKey);
    newUser.setActivated(false);
    newUser.setActivationKey(RandomUtil.generateActivationKey());
    authorities.add(authority);
    newUser.setAuthorities(authorities);
    userRepository.save(newUser);
    userSearchRepository.save(newUser);
    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  public User createUser(ManagedUserDTO managedUserDTO) {
    User user = new User();
    user.setLogin(managedUserDTO.getLogin());
    user.setFirstName(managedUserDTO.getFirstName());
    user.setLastName(managedUserDTO.getLastName());
    user.setEmail(managedUserDTO.getEmail());
    if (managedUserDTO.getLangKey() == null) {
      user.setLangKey("en");
    } else {
      user.setLangKey(managedUserDTO.getLangKey());
    }
    Set<Authority> authorities = new HashSet<>();
    managedUserDTO.getAuthorities().stream().forEach(
      authority -> authorities.add(authorityRepository.findOne(authority))
    );
    user.setAuthorities(authorities);
    String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
    user.setPassword(encryptedPassword);
    user.setResetKey(RandomUtil.generateResetKey());
    user.setResetDate(ZonedDateTime.now());
    user.setActivated(true);
    userRepository.save(user);
    log.debug("Created Information for User: {}", user);
    return user;
  }

  public void updateUserInformation(String firstName, String lastName, String email, String langKey, String pictureContentType, byte[] picture) {
    userRepository.findOneByLogin(SecurityUtil.getCurrentUserLogin()).ifPresent(u -> {
      u.setFirstName(firstName);
      u.setLastName(lastName);
      u.setEmail(email);
      u.setLangKey(langKey);
      u.setPictureContentType(pictureContentType);
      u.setPicture(picture);
      userRepository.save(u);
      userSearchRepository.save(u);
      log.debug("Changed Information for User: {}", u);
    });
  }

  public void deleteUserInformation(String login) {
    userRepository.findOneByLogin(login).ifPresent(u -> {
      userRepository.delete(u);
      userSearchRepository.delete(u);
      log.debug("Deleted User: {}", u);
    });
  }

  public void changePassword(String password) {
    userRepository.findOneByLogin(SecurityUtil.getCurrentUserLogin()).ifPresent(u -> {
      String encryptedPassword = passwordEncoder.encode(password);
      u.setPassword(encryptedPassword);
      userRepository.save(u);
      log.debug("Changed password for User: {}", u);
    });
  }

  @Transactional(readOnly = true)
  public Optional<User> getUserWithAuthoritiesByLogin(String login) {
    return userRepository.findOneByLogin(login).map(u -> {
      u.getAuthorities().size();
      return u;
    });
  }

  @Transactional(readOnly = true)
  public User getUserWithAuthorities(Long id) {
    User user = userRepository.findOne(id);
    user.getAuthorities().size(); // eagerly load the association
    return user;
  }

  @Transactional(readOnly = true)
  public User getUserWithAuthorities() {
    User user = userRepository.findOneByLogin(SecurityUtil.getCurrentUserLogin()).get();
    user.getAuthorities().size(); // eagerly load the association
    return user;
  }

  /**
   * Persistent Token are used for providing automatic authentication, they should be automatically deleted after 30 days.
   * <p/> <p> This is scheduled to get fired everyday, at midnight. </p>
   */
  @Scheduled(cron = "0 0 0 * * ?")
  public void removeOldPersistentTokens() {
    LocalDate now = LocalDate.now();
    persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).stream().forEach(token -> {
      log.debug("Deleting token {}", token.getSeries());
      User user = token.getUser();
      user.getPersistentTokens().remove(token);
      persistentTokenRepository.delete(token);
    });
  }

  /**
   * Not activated users should be automatically deleted after 3 days. <p/> <p> This is scheduled to get fired everyday, at
   * 01:00 (am). </p>
   */
  @Scheduled(cron = "0 0 1 * * ?")
  public void removeNotActivatedUsers() {
    ZonedDateTime now = ZonedDateTime.now();
    List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
    for (User user : users) {
      log.debug("Deleting not activated user {}", user.getLogin());
      userRepository.delete(user);
      userSearchRepository.delete(user);
    }
  }
}
