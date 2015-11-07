package com.priitlaht.maurus.backend.service;

import com.priitlaht.maurus.common.config.audit.AuditEventConverter;
import com.priitlaht.maurus.backend.domain.PersistentAuditEvent;
import com.priitlaht.maurus.backend.repository.PersistenceAuditEventRepository;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

@Service
@Transactional
public class AuditEventService {
  private PersistenceAuditEventRepository persistenceAuditEventRepository;
  private AuditEventConverter auditEventConverter;

  @Inject
  public AuditEventService(PersistenceAuditEventRepository persistenceAuditEventRepository, AuditEventConverter auditEventConverter) {
    this.persistenceAuditEventRepository = persistenceAuditEventRepository;
    this.auditEventConverter = auditEventConverter;
  }

  public List<AuditEvent> findAll() {
    return auditEventConverter.convertToAuditEvent(persistenceAuditEventRepository.findAll());
  }

  public List<AuditEvent> findByDates(LocalDateTime fromDate, LocalDateTime toDate) {
    List<PersistentAuditEvent> persistentAuditEvents = persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate);
    return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
  }

  public Optional<AuditEvent> find(Long id) {
    return Optional.ofNullable(persistenceAuditEventRepository.findOne(id)).map(auditEventConverter::convertToAuditEvent);
  }
}
