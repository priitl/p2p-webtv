package com.priitlaht.ppwebtv.backend.repository;

import com.priitlaht.ppwebtv.backend.domain.SocialUserConnection;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SocialUserConnectionRepository extends JpaRepository<SocialUserConnection, Long> {

  List<SocialUserConnection> findAllByProviderIdAndProviderUserId(String providerId, String providerUserId);

  List<SocialUserConnection> findAllByProviderIdAndProviderUserIdIn(String providerId, Set<String> providerUserIds);

  List<SocialUserConnection> findAllByUserIdOrderByProviderIdAscRankAsc(String userId);

  List<SocialUserConnection> findAllByUserIdAndProviderIdOrderByRankAsc(String userId, String providerId);

  List<SocialUserConnection> findAllByUserIdAndProviderIdAndProviderUserIdIn(String userId, String providerId, List<String> provideUserId);

  SocialUserConnection findOneByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);

  void deleteByUserIdAndProviderId(String userId, String providerId);

  void deleteByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);
}
