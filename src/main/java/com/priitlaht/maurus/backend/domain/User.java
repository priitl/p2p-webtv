package com.priitlaht.maurus.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Cache;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import static javax.persistence.GenerationType.AUTO;
import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

@Getter
@Setter
@Entity
@Table(name = "jhi_user")
@Document(indexName = "user")
@Cache(usage = NONSTRICT_READ_WRITE)
public class User extends BaseAuditingEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = AUTO)
  private Long id;

  @NotNull
  @Size(min = 1, max = 100)
  @Column(length = 100, unique = true, nullable = false)
  private String login;

  @JsonIgnore
  @NotNull
  @Size(min = 60, max = 60)
  @Column(name = "password_hash", length = 60)
  private String password;

  @Size(max = 50)
  @Column(name = "first_name", length = 50)
  private String firstName;

  @Size(max = 50)
  @Column(name = "last_name", length = 50)
  private String lastName;

  @Email
  @Size(max = 100)
  @Column(length = 100, unique = true)
  private String email;

  @Column(nullable = false)
  private boolean activated = false;

  @Size(min = 2, max = 5)
  @Column(name = "lang_key", length = 5)
  private String langKey;

  @JsonIgnore
  @Size(max = 20)
  @Column(name = "activation_key", length = 20)
  private String activationKey;

  @Size(max = 20)
  @Column(name = "reset_key", length = 20)
  private String resetKey;

  @Column(name = "reset_date", nullable = true)
  private ZonedDateTime resetDate = null;

  @Lob
  @Column(name = "picture")
  private byte[] picture;

  @Column(name = "picture_content_type")
  private String pictureContentType;

  @JsonIgnore
  @ManyToMany
  @JoinTable(
    name = "jhi_user_authority",
    joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
  @Cache(usage = NONSTRICT_READ_WRITE)
  private Set<Authority> authorities = new HashSet<>();

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
  @Cache(usage = NONSTRICT_READ_WRITE)
  private Set<PersistentToken> persistentTokens = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    } else {
      User user = (User) o;
      return login.equals(user.login);
    }
  }

  @Override
  public int hashCode() {
    return login.hashCode();
  }

  @Override
  public String toString() {
    return "User{" +
      "login='" + login + '\'' +
      ", firstName='" + firstName + '\'' +
      ", lastName='" + lastName + '\'' +
      ", email='" + email + '\'' +
      ", activated='" + activated + '\'' +
      ", langKey='" + langKey + '\'' +
      ", activationKey='" + activationKey + '\'' +
      ", pictureContentType='" + pictureContentType + "'" +
      "}";
  }
}
