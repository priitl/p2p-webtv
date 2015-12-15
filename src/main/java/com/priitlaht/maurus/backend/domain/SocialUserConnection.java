package com.priitlaht.maurus.backend.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "jhi_social_user_connection")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SocialUserConnection implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(name = "user_id", length = 255, nullable = false)
  private String userId;

  @NotNull
  @Column(name = "provider_id", length = 255, nullable = false)
  private String providerId;

  @NotNull
  @Column(name = "provider_user_id", length = 255, nullable = false)
  private String providerUserId;

  @NotNull
  @Column(nullable = false)
  private Long rank;

  @Column(name = "display_name", length = 255)
  private String displayName;

  @Column(name = "profile_url", length = 255)
  private String profileUrl;

  @Column(name = "image_url", length = 255)
  private String imageURL;

  @NotNull
  @Column(name = "access_token", length = 255, nullable = false)
  private String accessToken;

  @Column(length = 255)
  private String secret;

  @Column(name = "refresh_token", length = 255)
  private String refreshToken;

  @Column(name = "expire_time")
  private Long expireTime;

  public SocialUserConnection(String userId, String providerId, String providerUserId, Long rank, String displayName, String profileUrl,
                              String imageURL, String accessToken, String secret, String refreshToken, Long expireTime) {
    this.userId = userId;
    this.providerId = providerId;
    this.providerUserId = providerUserId;
    this.rank = rank;
    this.displayName = displayName;
    this.profileUrl = profileUrl;
    this.imageURL = imageURL;
    this.accessToken = accessToken;
    this.secret = secret;
    this.refreshToken = refreshToken;
    this.expireTime = expireTime;
  }

}
