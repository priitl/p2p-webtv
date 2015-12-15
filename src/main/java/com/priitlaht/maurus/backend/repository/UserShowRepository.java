package com.priitlaht.maurus.backend.repository;

import com.priitlaht.maurus.backend.domain.UserShow;
import com.priitlaht.maurus.backend.domain.UserShowId;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Priit Laht
 */
public interface UserShowRepository extends JpaRepository<UserShow, UserShowId> {

  List<UserShow> findAllByUserLogin(String userLogin);

  void deleteByUserLoginAndShowName(String userLogin, String showName);
}
