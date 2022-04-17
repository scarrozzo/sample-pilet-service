-- liquibase formatted sql

-- changeset resevo-piletfeed-service:1
create table PILET
(
    ID                 BIGINT       not null
        primary key,
    RS                 CHARACTER(1) not null,
    VERSION_           BIGINT,
    CREATED_DATE       DATE         not null,
    CREATED_TIME       TIME         not null,
    CREATOR            VARCHAR(96) not null,
    LAST_MODIFIED_DATE DATE         not null,
    LAST_MODIFIED_TIME TIME         not null,
    MODIFIER           VARCHAR(96) not null,
    UID                VARCHAR(64)  not null
        constraint UK_P0ICVYL2TTCHHHCIS54GCAFMB
            unique,
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
    TYPE               VARCHAR(255)
);

-- changeset resevo-piletfeed-service:2
create table FILE
(
    ID           BIGINT not null
        primary key,
    FILE_CONTENT CLOB(10M),
    FILE_NAME    VARCHAR(255),
    PILET_ID     BIGINT
        constraint FKQ4G04T5X0P0P6NCFAQXFC4T8E
            references PILET
);

-- changeset resevo-piletfeed-service:3
create index IDX_PILET_APPSHELL
    on PILET (APPSHELL);

-- changeset resevo-piletfeed-service:4
create unique index IDX_PILET_APPSHELL_NAME_VERSION
    on PILET (APPSHELL, NAME, PACKAGE_VERSION);

-- changeset resevo-piletfeed-service:5
create sequence FILE_SEQ;

-- changeset resevo-piletfeed-service:6
create sequence PILET_SEQ;
