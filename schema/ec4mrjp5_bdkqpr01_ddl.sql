DROP TABLE `BDKQPR01`;
CREATE TABLE `BDKQPR01` (
  `ID` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID'
, `EMPLOYEE_FIRST_NAME` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '이름'
, `EMPLOYEE_LAST_NAME` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '성'
, `COMPANY_NAME` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '회사명'
, `COMPANY_ADDRESS` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '회사 주소'
, `COMPANY_CITY` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '회사 도시'
, `COMPANY_COUNTY` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '회사 카운티'
, `COMPANY_STATE` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '회사 주'
, `COMPANY_ZIP` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '회사 우편번호'
, `COMPANY_PHONE1` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '회사 전화번호'
, `PERSONAL_PHONE2` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '개인 전화번호'
, `PERSONAL_EMAIL` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '개인 이메일'
, `COMPANY_WEB` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '회사 URL'
, `CREATE_USER` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '최초 등록 사용자'
, `CREATE_TIME` timestamp NULL DEFAULT NULL COMMENT '최초 등록 시간'
, `UPDATE_USER` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '최종 수정 사용자'
, `UPDATE_TIME` timestamp NULL DEFAULT NULL COMMENT '최종 수정 시간'
, PRIMARY KEY (`ID`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'us-500.csv 배치 테스트';
