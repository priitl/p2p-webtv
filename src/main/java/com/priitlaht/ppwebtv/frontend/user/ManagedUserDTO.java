package com.priitlaht.ppwebtv.frontend.user;

import com.priitlaht.ppwebtv.backend.domain.User;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A DTO extending the UserDTO, which is meant to be used in the user management UI.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ManagedUserDTO extends UserDTO {
  private Long id;
  private ZonedDateTime createdDate;
  private String lastModifiedBy;
  private ZonedDateTime lastModifiedDate;

  public ManagedUserDTO(User user) {
    super(user);
    this.id = user.getId();
    this.createdDate = user.getCreatedDate();
    this.lastModifiedBy = user.getLastModifiedBy();
    this.lastModifiedDate = user.getLastModifiedDate();
  }
}
