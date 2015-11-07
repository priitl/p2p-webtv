package com.priitlaht.maurus.backend.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
@Table(name = "jhi_persistent_audit_event")
public class PersistentAuditEvent {

  @Id
  @GeneratedValue(strategy = AUTO)
  @Column(name = "event_id")
  private Long id;

  @NotNull
  @Column(nullable = false)
  private String principal;

  @Column(name = "event_type")
  private String auditEventType;

  @Column(name = "event_date")
  private LocalDateTime auditEventDate;

  @ElementCollection
  @MapKeyColumn(name = "name")
  @Column(name = "value")
  @CollectionTable(name = "jhi_persistent_audit_evt_data", joinColumns = @JoinColumn(name = "event_id"))
  private Map<String, String> data = new HashMap<>();

}
