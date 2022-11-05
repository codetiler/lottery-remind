-- auto-generated definition
create table BETTING_INFO
(
    ID      CHARACTER VARYING(255) not null
        primary key,
    BLUE    CHARACTER VARYING(255),
    REDS    CHARACTER VARYING(255),
    USER_ID CHARACTER VARYING(255)
);

-- auto-generated definition
create table LOTTERY
(
    ID    CHARACTER VARYING(255) not null
        primary key,
    BLUE  CHARACTER VARYING(255),
    ISSUE CHARACTER VARYING(255),
    REDS  CHARACTER VARYING(255)
);

-- auto-generated definition
create table SYS_USER
(
    ID    CHARACTER VARYING(255) not null
        primary key,
    EMAIL CHARACTER VARYING(255),
    NAME  CHARACTER VARYING(255)
);

-- auto-generated definition
create table LOTTERY_HISTORY
(
    ID    CHARACTER VARYING(255) not null
        primary key,
    BLUE  CHARACTER VARYING(255),
    ISSUE CHARACTER VARYING(255),
    REDS  CHARACTER VARYING(255)
);
