package com.priitlaht.maurus.backend.repository;

import com.priitlaht.maurus.backend.domain.PersistentToken;
import com.priitlaht.maurus.backend.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {

  List<PersistentToken> findByUser(User user);

  List<PersistentToken> findByTokenDateBefore(LocalDate localDate);

}
