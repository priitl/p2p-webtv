package com.priitlaht.maurus.frontend.account;

import com.codahale.metrics.annotation.Timed;
import com.priitlaht.maurus.backend.domain.PersistentToken;
import com.priitlaht.maurus.backend.domain.User;
import com.priitlaht.maurus.backend.repository.PersistentTokenRepository;
import com.priitlaht.maurus.backend.repository.UserRepository;
import com.priitlaht.maurus.backend.service.MailService;
import com.priitlaht.maurus.backend.service.UserService;
import com.priitlaht.maurus.common.util.security.SecurityUtil;
import com.priitlaht.maurus.frontend.user.UserDTO;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

@Slf4j
@RestController
@RequestMapping("/api")
public class AccountResource {

  @Inject
  private UserRepository userRepository;
  @Inject
  private UserService userService;
  @Inject
  private PersistentTokenRepository persistentTokenRepository;
  @Inject
  private MailService mailService;

  @Timed
  @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<?> registerAccount(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
    return userRepository.findOneByLogin(userDTO.getLogin())
      .map(user -> new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST))
      .orElseGet(() -> userRepository.findOneByEmail(userDTO.getEmail())
          .map(user -> new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST))
          .orElseGet(() -> {
            User user = userService.createUserInformation(userDTO.getLogin(), userDTO.getPassword(),
              userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail().toLowerCase(),
              userDTO.getLangKey());
            String baseUrl = format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());

            mailService.sendActivationEmail(user, baseUrl);
            return new ResponseEntity<>(HttpStatus.CREATED);
          })
      );
  }

  @Timed
  @RequestMapping(value = "/activate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
    return Optional.ofNullable(userService.activateRegistration(key))
      .map(user -> new ResponseEntity<String>(HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @Timed
  @RequestMapping(value = "/authenticate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public String isAuthenticated(HttpServletRequest request) {
    log.debug("REST request to check if the current user is authenticated");
    return request.getRemoteUser();
  }

  @Timed
  @RequestMapping(value = "/account", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> getAccount() {
    return Optional.ofNullable(userService.getUserWithAuthorities())
      .map(user -> new ResponseEntity<>(new UserDTO(user), HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @Timed
  @RequestMapping(value = "/account", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> saveAccount(@RequestBody UserDTO userDTO) {
    return userRepository
      .findOneByLogin(userDTO.getLogin())
      .filter(u -> u.getLogin().equals(SecurityUtil.getCurrentUserLogin()))
      .map(u -> {
        userService.updateUserInformation(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
          userDTO.getLangKey(), userDTO.getPictureContentType(), userDTO.getPicture());
        return new ResponseEntity<String>(HttpStatus.OK);
      })
      .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @Timed
  @RequestMapping(value = "/account/change_password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> changePassword(@RequestBody String password) {
    if (!checkPasswordLength(password)) {
      return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
    }
    userService.changePassword(password);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Timed
  @RequestMapping(value = "/account/sessions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PersistentToken>> getCurrentSessions() {
    return userRepository.findOneByLogin(SecurityUtil.getCurrentUserLogin())
      .map(user -> new ResponseEntity<>(
        persistentTokenRepository.findByUser(user),
        HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  /**
   * DELETE  /account/sessions?series={series} -> invalidate an existing session.
   *
   * - You can only delete your own sessions, not any other user's session - If you delete one of your existing sessions, and
   * that you are currently logged in on that session, you will still be able to use that session, until you quit your
   * browser: it does not work in real time (there is no API for that), it only removes the "remember me" cookie - This is
   * also true if you invalidate your current session: you will still be able to use it until you close your browser or that
   * the session times out. But automatic login (the "remember me" cookie) will not work anymore. There is an API to
   * invalidate the current session, but there is no API to check which session uses which cookie.
   */
  @Timed
  @RequestMapping(value = "/account/sessions/{series}", method = RequestMethod.DELETE)
  public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
    String decodedSeries = URLDecoder.decode(series, "UTF-8");
    userRepository.findOneByLogin(SecurityUtil.getCurrentUserLogin()).ifPresent(u -> {
      persistentTokenRepository.findByUser(u).stream()
        .filter(persistentToken -> StringUtils.equals(persistentToken.getSeries(), decodedSeries))
        .findAny().ifPresent(t -> persistentTokenRepository.delete(decodedSeries));
    });
  }

  @Timed
  @RequestMapping(value = "/account/reset_password/init", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<?> requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {
    return userService.requestPasswordReset(mail)
      .map(user -> {
        String baseUrl = format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
        mailService.sendPasswordResetMail(user, baseUrl);
        return new ResponseEntity<>("e-mail was sent", HttpStatus.OK);
      }).orElse(new ResponseEntity<>("e-mail address not registered", HttpStatus.BAD_REQUEST));
  }

  @Timed
  @RequestMapping(value = "/account/reset_password/finish", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordDTO keyAndPassword) {
    if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
      return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
    }
    return userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
      .map(user -> new ResponseEntity<String>(HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  private boolean checkPasswordLength(String password) {
    return (!StringUtils.isEmpty(password) &&
      password.length() >= UserDTO.PASSWORD_MIN_LENGTH &&
      password.length() <= UserDTO.PASSWORD_MAX_LENGTH);
  }
}
