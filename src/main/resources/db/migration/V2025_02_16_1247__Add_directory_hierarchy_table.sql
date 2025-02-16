
CREATE TABLE DIRECTORY
(
    directory_id bigint primary key,
    directory_name varchar(255) not null,
    directory_uuid varchar(255) not null,
    parent_directory_id bigint references directory (directory_id)
        on delete cascade,
    owner varchar(255) not null
);

CREATE SEQUENCE S_DIRECTORY START WITH 1;
