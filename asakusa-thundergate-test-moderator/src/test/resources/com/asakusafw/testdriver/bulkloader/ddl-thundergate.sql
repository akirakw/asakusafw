CREATE  TABLE __TG_CACHE_INFO (
    CACHE_ID VARCHAR(128) NOT NULL,
    CACHE_TIMESTAMP DATETIME NOT NULL,
    BUILT_TIMESTAMP DATETIME NOT NULL,
    TABLE_NAME VARCHAR(64) NOT NULL,
    REMOTE_PATH VARCHAR(255) NOT NULL,
    PRIMARY KEY (CACHE_ID)
);

CREATE  TABLE __TG_CACHE_LOCK (
    CACHE_ID VARCHAR(128) NOT NULL,
    EXECUTION_ID VARCHAR(128) NOT NULL,
    ACQUIRED DATETIME NOT NULL,
    PRIMARY KEY (CACHE_ID)
);
