create table users(
    id uuid not null primary key,
    name varchar(150) not null,
    email varchar(100) not null,
    created_at timestamp without time zone not null default now()
);

create unique index users_email_idx on users using btree(email);

create table events(
    id serial primary key,
    type varchar(50) not null,
    content text not null,
    created_at timestamp without time zone not null default now()
);
