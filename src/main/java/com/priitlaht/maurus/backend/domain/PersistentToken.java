package com.priitlaht.maurus.backend.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Cache;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

@Data
@Entity
@Table(name = "jhi_persistent_token")
@Cache(usage = NONSTRICT_READ_WRITE)
public class PersistentToken implements Serializable {
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d MMMM yyyy");
  private static final int MAX_USER_AGENT_LEN = 255;

  @Id
  private String series;

  @JsonIgnore
  @NotNull
  @Column(name = "token_value", nullable = false)
  private String tokenValue;

  @JsonIgnore
  @Column(name = "token_date")
  private LocalDate tokenDate;

  @Size(min = 0, max = 39)
  @Column(name = "ip_address", length = 39)
  private String ipAddress;

  @Column(name = "user_agent")
  private String userAgent;

  @JsonIgnore
  @ManyToOne
  private User user;

  @JsonGetter
  public String getFormattedTokenDate() {
    return DATE_TIME_FORMATTER.format(this.tokenDate);
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent.length() >= MAX_USER_AGENT_LEN ? userAgent.substring(0, MAX_USER_AGENT_LEN - 1) : userAgent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    } else {
      PersistentToken that = (PersistentToken) o;
      return series.equals(that.series);
    }
  }

  @Override
  public int hashCode() {
    return series.hashCode();
  }

}
