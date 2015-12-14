package com.priitlaht.maurus.frontend.user;

import com.codahale.metrics.annotation.Timed;
import com.priitlaht.maurus.backend.domain.Authority;
import com.priitlaht.maurus.backend.domain.User;
import com.priitlaht.maurus.backend.repository.AuthorityRepository;
import com.priitlaht.maurus.backend.repository.UserRepository;
import com.priitlaht.maurus.backend.repository.search.UserSearchRepository;
import com.priitlaht.maurus.backend.service.MailService;
import com.priitlaht.maurus.backend.service.UserService;
import com.priitlaht.maurus.common.AuthoritiesConstants;
import com.priitlaht.maurus.frontend.common.util.HeaderUtil;
import com.priitlaht.maurus.frontend.common.util.PaginationUtil;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing users.
 *
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p> <p> For a normal use-case,
 * it would be better to have an eager relationship between User and Authority, and send everything to the client side: there
 * would be no DTO, a lot less code, and an outer-join which would be good for performance. </p> <p> We use a DTO for 3
 * reasons: <ul> <li>We want to keep a lazy association between the user and the authorities, because people will quite often
 * do relationships with the user, and we don't want them to get the authorities all the time for nothing (for performance
 * reasons). This is the #1 goal: we should not impact our users' application because of this use-case.</li> <li> Not having
 * an outer join causes n+1 requests to the database. This is not a real issue as we have by default a second-level cache.
 * This means on the first HTTP call we do the n+1 requests, but then all authorities come from the cache, so in fact it's
 * much better than doing an outer join (which will get lots of data from the database, for each HTTP call).</li> <li> As
 * this manages users, for security reasons, we'd rather have a DTO layer.</li> </p> <p>Another option would be to have a
 * specific JPA entity graph to handle this case.</p>
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class UserResource {

  @Inject
  private UserRepository userRepository;
  @Inject
  private AuthorityRepository authorityRepository;
  @Inject
  private UserService userService;
  @Inject
  private UserSearchRepository userSearchRepository;
  @Inject
  private MailService mailService;

  @Timed
  @Secured(AuthoritiesConstants.ADMIN)
  @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createUser(@RequestBody ManagedUserDTO managedUserDTO, HttpServletRequest request) throws URISyntaxException {
    log.debug("REST request to save User : {}", managedUserDTO);
    if (userRepository.findOneByLogin(managedUserDTO.getLogin()).isPresent()) {
      return ResponseEntity.badRequest()
        .headers(HeaderUtil.createFailureAlert("user-management", "userexists", "Login already in use"))
        .body(null);
    } else if (userRepository.findOneByEmail(managedUserDTO.getEmail()).isPresent()) {
      return ResponseEntity.badRequest()
        .headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use"))
        .body(null);
    } else {
      User newUser = userService.createUser(managedUserDTO);
      String baseUrl = format("%s://%s:%d%s", request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath());
      mailService.sendCreationEmail(newUser, baseUrl);
      return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
        .headers(HeaderUtil.createAlert("user-management.created", newUser.getLogin()))
        .body(newUser);
    }
  }

  @Timed
  @Transactional
  @Secured(AuthoritiesConstants.ADMIN)
  @RequestMapping(value = "/users", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ManagedUserDTO> updateUser(@RequestBody ManagedUserDTO managedUserDTO) throws URISyntaxException {
    log.debug("REST request to update User : {}", managedUserDTO);
    Optional<User> existingUser = userRepository.findOneByEmail(managedUserDTO.getEmail());
    if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(managedUserDTO.getLogin()))) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use")).body(null);
    }
    return userRepository
      .findOneById(managedUserDTO.getId())
      .map(user -> {
        user.setLogin(managedUserDTO.getLogin());
        user.setFirstName(managedUserDTO.getFirstName());
        user.setLastName(managedUserDTO.getLastName());
        user.setEmail(managedUserDTO.getEmail());
        user.setActivated(managedUserDTO.isActivated());
        user.setLangKey(managedUserDTO.getLangKey());
        Set<Authority> authorities = user.getAuthorities();
        authorities.clear();
        managedUserDTO.getAuthorities().stream().forEach(
          authority -> authorities.add(authorityRepository.findOne(authority))
        );
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("user-management.updated", managedUserDTO.getLogin()))
          .body(new ManagedUserDTO(userRepository.findOne(managedUserDTO.getId())));
      })
      .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @Timed
  @Transactional(readOnly = true)
  @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ManagedUserDTO>> getAllUsers(Pageable pageable) throws URISyntaxException {
    Page<User> page = userRepository.findAll(pageable);
    List<ManagedUserDTO> managedUserDTOs = page.getContent().stream()
      .map(ManagedUserDTO::new)
      .collect(Collectors.toList());
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
    return new ResponseEntity<>(managedUserDTOs, headers, HttpStatus.OK);
  }

  @Timed
  @RequestMapping(value = "/users/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ManagedUserDTO> getUser(@PathVariable String login) {
    log.debug("REST request to get User : {}", login);
    return userService.getUserWithAuthoritiesByLogin(login)
      .map(ManagedUserDTO::new)
      .map(managedUserDTO -> new ResponseEntity<>(managedUserDTO, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Timed
  @Secured(AuthoritiesConstants.ADMIN)
  @RequestMapping(value = "/users/{login}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> deleteUser(@PathVariable String login) {
    log.debug("REST request to delete User: {}", login);
    userService.deleteUserInformation(login);
    return ResponseEntity.ok().headers(HeaderUtil.createAlert("user-management.deleted", login)).build();
  }

  @Timed
  @RequestMapping(value = "/_search/users/{query}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<User> search(@PathVariable String query) {
    return StreamSupport
      .stream(userSearchRepository.search(queryStringQuery(query)).spliterator(), false)
      .collect(Collectors.toList());
  }
}
