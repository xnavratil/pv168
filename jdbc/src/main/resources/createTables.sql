CREATE TABLE MISSION (
    ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    CODENAME VARCHAR(50),
    INFO VARCHAR(50),
    ISSUE_DATE DATE
);

CREATE TABLE AGENT (
    ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    NAME VARCHAR(50),
    BORN DATE,
    RECRUITMENT_DATE DATE
);

CREATE TABLE ASSIGNMENT (
    ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    MISSION_ID BIGINT,
    AGENT_ID BIGINT,
    START DATE,
    EXPECTED_END DATE
);