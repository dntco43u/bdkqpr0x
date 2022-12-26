package com.fhy8vp3u.bdkqpr0x.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SystemEventsDTO {
  private String id;
  private String customerId;
  private String receivedAt;
  private String deviceReportedTime;
  private String facility;
  private String priority;
  private String fromHost;
  private String message;
  private String ntSeverity;
  private String importance;
  private String eventSource;
  private String eventUser;
  private String eventCategory;
  private String eventId;
  private String eventBinaryData;
  private String maxAvailable;
  private String currUsage;
  private String minUsage;
  private String maxUsage;
  private String infoUnitId;
  private String sysLogTag;
  private String eventLogType;
  private String genericFileName;
  private String systemId;
  private String createUser;
  private String createTime;
  private String updateUser;
  private String updateTime;
}
