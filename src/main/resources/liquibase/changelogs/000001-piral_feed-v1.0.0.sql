-- liquibase formatted sql

-- changeset piral-feed:1
CREATE TABLE PILET
(
    ID                 BIGINT       NOT NULL    AUTO_INCREMENT,
    VERSION_           BIGINT,
    CREATED_DATE       DATE         NOT NULL,
    CREATED_TIME       TIME         NOT NULL,
    CREATOR            VARCHAR(96)  NOT NULL,
    LAST_MODIFIED_DATE DATE         NOT NULL,
    LAST_MODIFIED_TIME TIME         NOT NULL,
    MODIFIER           VARCHAR(96)  NOT NULL,
    UID                VARCHAR(64)  NOT NULL,
    APPSHELL           VARCHAR(255),
    AUTHOR             VARCHAR(255),
    DEPENDENCIES       VARCHAR(255),
    DESCRIPTION        VARCHAR(255),
    INTEGRITY          VARCHAR(255),
    LICENSE_TEXT       VARCHAR(255),
    LICENSE_TYPE       VARCHAR(255),
    NAME               VARCHAR(255),
    PACKAGE_VERSION    VARCHAR(255),
    REQUIRE_REF        VARCHAR(255),
    TYPE               VARCHAR(255),
    UNIQUE(UID),
    PRIMARY KEY(ID),
    INDEX idx_pilet_appshell(APPSHELL),
    UNIQUE KEY idx_pilet_appshell_name_version(APPSHELL, NAME, PACKAGE_VERSION)
);

-- changeset piral-feed:2
CREATE TABLE FILE
(
    ID           BIGINT NOT NULL AUTO_INCREMENT,
    FILE_CONTENT LONGTEXT,
    FILE_NAME    VARCHAR(255),
    PILET_ID     BIGINT,
    PRIMARY KEY(ID),
    FOREIGN KEY(PILET_ID) REFERENCES PILET(ID),
    INDEX(PILET_ID),
    INDEX(ID, PILET_ID),
    INDEX(FILE_NAME)
);