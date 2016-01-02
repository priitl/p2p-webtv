package com.priitlaht.ppwebtv.backend.dto.user;

import com.priitlaht.ppwebtv.backend.domain.Authority;
import com.priitlaht.ppwebtv.backend.domain.User;

import org.hibernate.validator.constraints.Email;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representing a user, with his authorities.
 */
@Data
@NoArgsConstructor
public class UserDTO {
  public static final int PASSWORD_MIN_LENGTH = 5;
  public static final int PASSWORD_MAX_LENGTH = 100;

  @Pattern(regexp = "^[a-z0-9]*$")
  @NotNull
  @Size(min = 1, max = 50)
  private String login;

  @NotNull
  @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
  private String password;

  @Size(max = 50)
  private String firstName;

  @Size(max = 50)
  private String lastName;

  @Email
  @Size(min = 5, max = 100)
  private String email;

  private boolean activated = false;

  @Size(min = 2, max = 5)
  private String langKey;

  private Set<String> authorities;

  @Lob
  private byte[] picture;

  private String pictureContentType;

  private String apiUrl;

  public UserDTO(User user) {
    this(user.getLogin(), null, user.getFirstName(), user.getLastName(),
      user.getEmail(), user.isActivated(), user.getLangKey(),
      user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()),
      user.getPictureContentType(), user.getPicture());
  }

  public UserDTO(String login, String password, String firstName, String lastName,
                 String email, boolean activated, String langKey, Set<String> authorities,
                 String pictureContentType, byte[] picture) {

    this.login = login;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.activated = activated;
    this.langKey = langKey;
    this.authorities = authorities;
    this.pictureContentType = pictureContentType;
    this.picture = picture;
  }

  @Override
  public String toString() {
    return "UserDTO{" +
      "login='" + login + '\'' +
      ", firstName='" + firstName + '\'' +
      ", lastName='" + lastName + '\'' +
      ", email='" + email + '\'' +
      ", activated=" + activated +
      ", langKey='" + langKey + '\'' +
      ", authorities=" + authorities +
      "}";
  }
}
