DROP TABLE `SYSTEM_EVENTS`;

CREATE TABLE `SYSTEM_EVENTS` (
  `ID` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID'
  , `CUSTOMER_ID` BIGINT DEFAULT NULL COMMENT 'CUSTOMER ID'
  , `RECEIVED_AT` DATETIME DEFAULT NULL COMMENT '로그 수신 시간'
  , `DEVICE_REPORTED_TIME` DATETIME DEFAULT NULL COMMENT '로그 송신 시간'
  , `FACILITY` SMALLINT DEFAULT NULL COMMENT '서브시스템 유형'
  , `PRIORITY` SMALLINT DEFAULT NULL COMMENT '로그 레벨'
  , `FROM_HOST` VARCHAR(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '로그 송신 호스트명'
  , `MESSAGE` TEXT COLLATE utf8mb4_unicode_ci COMMENT '로그 메시지'
  , `NT_SEVERITY` INT DEFAULT NULL COMMENT 'NT SEVERITY'
  , `IMPORTANCE` INT DEFAULT NULL COMMENT 'IMPORTANCE'
  , `EVENT_SOURCE` VARCHAR(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'EVENT SOURCE'
  , `EVENT_USER` VARCHAR(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'EVEN _USER'
  , `EVENT_CATEGORY` INT DEFAULT NULL COMMENT 'EVENT CATEGORY'
  , `EVENT_ID` INT DEFAULT NULL COMMENT 'EVENT ID'
  , `EVENT_BINARY_DATA` TEXT COLLATE utf8mb4_unicode_ci COMMENT 'EVENT BINARY DATA'
  , `MAX_AVAILABLE` INT DEFAULT NULL COMMENT 'MAX AVAILABLE'
  , `CURR_USAGE` INT DEFAULT NULL COMMENT 'CURR USAGE'
  , `MIN_USAGE` INT DEFAULT NULL COMMENT 'MIN USAGE'
  , `MAX_USAGE` INT DEFAULT NULL COMMENT 'MAX USAGE'
  , `INFO_UNIT_ID` INT DEFAULT NULL COMMENT 'INFO UNIT ID'
  , `SYS_LOG_TAG` VARCHAR(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'syslog 태그'
  , `EVENT_LOG_TYPE` VARCHAR(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'EVENT LOG TYPE'
  , `GENERIC_FILE_NAME` VARCHAR(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'GENERIC FILE NAME'
  , `SYSTEM_ID` INT DEFAULT NULL COMMENT 'SYSTEM ID'
  , PRIMARY KEY (`ID`)
  ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'bd04_system_events.csv';
