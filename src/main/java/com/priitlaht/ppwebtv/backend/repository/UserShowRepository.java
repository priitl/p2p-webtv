package com.priitlaht.ppwebtv.backend.repository;

import com.priitlaht.ppwebtv.backend.domain.UserShow;
import com.priitlaht.ppwebtv.backend.domain.UserShowId;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Priit Laht
 */
public interface UserShowRepository extends JpaRepository<UserShow, UserShowId> {

  List<UserShow> findAllByUserLogin(String userLogin);

  void deleteByUserLoginAndTmdbId(String userLogin, Long tmdbId);
}
