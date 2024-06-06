DROP TABLE DEV.SYSTEM_EVENTS PURGE;

CREATE TABLE DEV.SYSTEM_EVENTS (
  "ID" NUMBER GENERATED BY DEFAULT AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE NOKEEP NOSCALE NOT NULL ENABLE
  , "CUSTOMER_ID" NUMBER(19, 0) DEFAULT NULL
  , "RECEIVED_AT" DATE DEFAULT NULL
  , "DEVICE_REPORTED_TIME" DATE DEFAULT NULL
  , "FACILITY" NUMBER(5, 0) DEFAULT NULL
  , "PRIORITY" NUMBER(5, 0) DEFAULT NULL
  , "FROM_HOST" VARCHAR2(60) COLLATE "USING_NLS_COMP" DEFAULT NULL
  , "MESSAGE" CLOB COLLATE "USING_NLS_COMP"
  , "NT_SEVERITY" NUMBER(10, 0) DEFAULT NULL
  , "IMPORTANCE" NUMBER(10, 0) DEFAULT NULL
  , "EVENT_SOURCE" VARCHAR2(60) COLLATE "USING_NLS_COMP" DEFAULT NULL
  , "EVENT_USER" VARCHAR2(60) COLLATE "USING_NLS_COMP" DEFAULT NULL
  , "EVENT_CATEGORY" NUMBER(10, 0) DEFAULT NULL
  , "EVENT_ID" NUMBER(10, 0) DEFAULT NULL
  , "EVENT_BINARY_DATA" CLOB COLLATE "USING_NLS_COMP"
  , "MAX_AVAILABLE" NUMBER(10, 0) DEFAULT NULL
  , "CURR_USAGE" NUMBER(10, 0) DEFAULT NULL
  , "MIN_USAGE" NUMBER(10, 0) DEFAULT NULL
  , "MAX_USAGE" NUMBER(10, 0) DEFAULT NULL
  , "INFO_UNIT_ID" NUMBER(10, 0) DEFAULT NULL
  , "SYS_LOG_TAG" VARCHAR2(60) COLLATE "USING_NLS_COMP" DEFAULT NULL
  , "EVENT_LOG_TYPE" VARCHAR2(60) COLLATE "USING_NLS_COMP" DEFAULT NULL
  , "GENERIC_FILE_NAME" VARCHAR2(60) COLLATE "USING_NLS_COMP" DEFAULT NULL
  , "SYSTEM_ID" NUMBER(10, 0) DEFAULT NULL
  , "CREATE_USER" VARCHAR2(8) COLLATE "USING_NLS_COMP" DEFAULT NULL
  , "CREATE_TIME" TIMESTAMP (0) DEFAULT NULL
  , "UPDATE_USER" VARCHAR2(8) COLLATE "USING_NLS_COMP" DEFAULT NULL
  , "UPDATE_TIME" TIMESTAMP (0) DEFAULT NULL
  , PRIMARY KEY ("ID")
  ) TABLESPACE "SAMPLESCHEMA" NOLOGGING;

COMMENT ON TABLE DEV.SYSTEM_EVENTS IS 'bd04_system_events.csv';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.ID IS 'ID';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.CUSTOMER_ID IS 'CUSTOMER ID';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.RECEIVED_AT IS '로그 수신 시간';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.DEVICE_REPORTED_TIME IS '로그 송신 시간';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.FACILITY IS '서브시스템 유형';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.PRIORITY IS '로그 레벨';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.FROM_HOST IS '로그 송신 호스트명';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.MESSAGE IS '로그 메시지';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.NT_SEVERITY IS 'NT SEVERITY';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.IMPORTANCE IS 'IMPORTANCE';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.EVENT_SOURCE IS 'EVENT SOURCE';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.EVENT_USER IS 'EVEN _USER';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.EVENT_CATEGORY IS 'EVENT CATEGORY';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.EVENT_ID IS 'EVENT ID';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.EVENT_BINARY_DATA IS 'EVENT BINARY DATA';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.MAX_AVAILABLE IS 'MAX AVAILABLE';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.CURR_USAGE IS 'CURR USAGE';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.MIN_USAGE IS 'MIN USAGE';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.MAX_USAGE IS 'MAX USAGE';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.INFO_UNIT_ID IS 'INFO UNIT ID';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.SYS_LOG_TAG IS 'syslog 태그';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.EVENT_LOG_TYPE IS 'EVENT LOG TYPE';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.GENERIC_FILE_NAME IS 'GENERIC FILE NAME';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.SYSTEM_ID IS 'SYSTEM ID';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.CREATE_USER IS '최초 등록 사용자';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.CREATE_TIME IS '최초 등록 시간';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.UPDATE_USER IS '최종 수정 사용자';
COMMENT ON COLUMN DEV.SYSTEM_EVENTS.UPDATE_TIME IS '최종 수정 시간';

SELECT 'ALTER INDEX ' || A.INDEX_NAME || ' RENAME TO SYSTEM_EVENTS_X01;'
FROM ALL_IND_COLUMNS A
  , ALL_INDEXES B
WHERE A.INDEX_NAME = B.INDEX_NAME
  AND A.TABLE_NAME = 'SYSTEM_EVENTS'
  AND A.COLUMN_NAME = 'ID';
