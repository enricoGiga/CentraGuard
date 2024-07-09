CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
create type role_type as enum ('user');
create table consumer
(
    consumer_id uuid default uuid_generate_v4() not null
        constraint consumer_pkey
            primary key,
    email       varchar(40),
    first_name  varchar(20)                     not null,
    last_name   varchar(20)                     not null
);

alter table consumer
    owner to enrico;

create table consumer_role
(
    consumer_id uuid default uuid_generate_v4() not null
        constraint fkgsaxe2yhkpe3ggxnseucdry2n
            references consumer,
    role_id     uuid default uuid_generate_v4() not null,
    constraint consumer_role_pkey
        primary key (consumer_id, role_id)
);

alter table consumer_role
    owner to enrico;

create table room
(
    room_id     uuid default uuid_generate_v4() not null
        constraint room_pkey
            primary key,
    description varchar(20),
    title       varchar(40)                     not null,
    consumer_id uuid default uuid_generate_v4() not null
        constraint fk5r21qg14ob0d4rd6nbngwyr4i
            references consumer
);

alter table room
    owner to enrico;

create table occurrence
(
    occurence_id uuid default uuid_generate_v4() not null
        constraint occurrence_pkey
            primary key,
    created_date timestamp,
    end_date     timestamp,
    name         varchar(15)                     not null,
    start_date   timestamp,
    room_id      uuid default uuid_generate_v4()
        constraint fk8h5hpfevmif0firutxxa73efq
            references room
            on delete cascade
);

alter table occurrence
    owner to enrico;

create table slot
(
    slot_id       uuid default uuid_generate_v4() not null
        constraint slot_pkey
            primary key,
    end_time      time,
    slot_interval bigint,
    start_time    time,
    consumer_id   uuid default uuid_generate_v4()
        constraint fkotg4pymph09r7ltyuxkmfwngd
            references consumer,
    occurrence_id uuid default uuid_generate_v4()
        constraint fk6smhsdqju884xe5jgx4rf6acw
            references occurrence
            on delete cascade
);

alter table slot
    owner to enrico;

