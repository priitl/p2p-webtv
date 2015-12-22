package com.priitlaht.ppwebtv.frontend.activity;

import lombok.Data;

/**
 * DTO for storing a user's activity.
 */
@Data
public class ActivityDTO {
  private String sessionId;
  private String userLogin;
  private String ipAddress;
  private String page;
  private String time;
}
