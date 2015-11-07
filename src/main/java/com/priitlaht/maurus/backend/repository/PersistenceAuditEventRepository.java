package com.priitlaht.maurus.backend.repository;

import com.priitlaht.maurus.backend.domain.PersistentAuditEvent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PersistenceAuditEventRepository extends JpaRepository<PersistentAuditEvent, Long> {

  List<PersistentAuditEvent> findByPrincipal(String principal);

  List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(String principal, LocalDateTime after);

  List<PersistentAuditEvent> findAllByAuditEventDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
