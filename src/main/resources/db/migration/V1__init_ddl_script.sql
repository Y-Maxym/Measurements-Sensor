create table if not exists public."user"
(
    id         bigserial    not null,
    created_at timestamp(6) not null,
    email      varchar(255) not null,
    password   varchar(255) not null,
    updated_at timestamp(6) not null,
    username   varchar(100) not null,
    primary key (id),
    constraint unique_user_username unique (username),
    constraint unique_user_email unique (email),
    constraint idx_user_username unique (username)
);

create table if not exists public.role
(
    id   bigserial    not null,
    role varchar(100) not null,
    primary key (id),
    constraint unique_role_role unique (role),
    constraint idx_role_role unique (role)
);

create table if not exists public.user_role
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    constraint fk_user_role_role_id FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    constraint fk_user_role_user_id FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);

create table if not exists public.refresh_token
(
    id          bigserial    not null,
    created_at  timestamp(6) not null,
    expiry_date timestamp(6) not null,
    token       varchar(255) not null,
    updated_at  timestamp(6) not null,
    user_id     bigint       not null,
    primary key (id),
    constraint unique_refresh_token_user_id unique (user_id),
    constraint fk_refresh_token_user_id FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);

create index if not exists idx_refresh_token_user_id
    on public.refresh_token (user_id);

create index if not exists idx_refresh_token_token
    on public.refresh_token (token);

create table if not exists public.sensor
(
    id         bigserial    not null,
    created_at timestamp(6) not null,
    name       varchar(100) not null,
    updated_at timestamp(6) not null,
    created_by bigint       not null,
    primary key (id),
    constraint unique_sensor_name unique (name),
    constraint idx_sensor_name unique (name),
    constraint fk_sensor_created_by FOREIGN KEY (created_by) REFERENCES "user"(id) ON DELETE CASCADE
);

create table if not exists public.measurement
(
    id               serial        not null,
    measurement_date timestamp(6)  not null,
    raining          boolean       not null,
    updated_at       timestamp(6)  not null,
    value            numeric(4, 1) not null,
    sensor_id        bigint        not null,
    primary key (id),
    constraint fk_measurement_sensor_id FOREIGN KEY (sensor_id) REFERENCES sensor(id) ON DELETE CASCADE
);