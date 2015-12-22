package com.priitlaht.ppwebtv.backend.repository;

import com.priitlaht.ppwebtv.backend.domain.PersistentToken;
import com.priitlaht.ppwebtv.backend.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {

  List<PersistentToken> findByUser(User user);

  List<PersistentToken> findByTokenDateBefore(LocalDate localDate);

}
