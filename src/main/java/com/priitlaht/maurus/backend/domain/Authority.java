package com.priitlaht.maurus.backend.domain;

import org.hibernate.annotations.Cache;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

@Data
@Entity
@Table(name = "jhi_authority")
@Cache(usage = NONSTRICT_READ_WRITE)
public class Authority implements Serializable {

  @Id
  @NotNull
  @Size(min = 0, max = 50)
  @Column(name = "name", length = 50)
  private String name;

}
