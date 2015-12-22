package com.priitlaht.ppwebtv.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Audited
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditingEntity {

  @JsonIgnore
  @NotNull
  @CreatedBy
  @Column(name = "created_by", nullable = false, length = 50, updatable = false)
  private String createdBy;

  @JsonIgnore
  @NotNull
  @CreatedDate
  @Column(name = "created_date", nullable = false)
  private ZonedDateTime createdDate = ZonedDateTime.now();

  @JsonIgnore
  @LastModifiedBy
  @Column(name = "last_modified_by", length = 50)
  private String lastModifiedBy;

  @JsonIgnore
  @LastModifiedDate
  @Column(name = "last_modified_date")
  private ZonedDateTime lastModifiedDate = ZonedDateTime.now();

}
