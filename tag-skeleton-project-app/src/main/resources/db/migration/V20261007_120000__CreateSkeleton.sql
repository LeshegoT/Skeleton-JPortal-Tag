CREATE SCHEMA IF NOT EXISTS skeleton;

CREATE TABLE skeleton.Skeleton
(
    Id              BIGSERIAL,
    Name            VARCHAR(255) NOT NULL,
    IsUpdated       BOOLEAN NOT NULL DEFAULT FALSE,
    LastUpdatedDate TIMESTAMP NOT NULL,

    CONSTRAINT Skeleton_PKey PRIMARY KEY (Id),
    CONSTRAINT Skeleton_Name_UKey UNIQUE (Name)
);

CREATE INDEX Skeleton_IsUpdated_Idx
    ON skeleton.Skeleton (IsUpdated);
