drop database test;
drop user tester;

create user tester with password 'admin';
create database test;

\connect test;

create table parent (
    parent_id uuid   primary key not null,
    first_name varchar (50) not null,
    last_name varchar (50) not null,
    email varchar (50) not null,
    password text not null
);

create table baby (
    baby_id uuid  primary key not null,
    first_name varchar not null,
    last_name varchar not null,
    birth_day date not null,
    feed_type integer not null
);

create table parent_baby (
    parent_id uuid not null,
    baby_id uuid  not null,
    CONSTRAINT P_B_KEY primary key (parent_id, baby_id)
);

create table baby_wight (
    baby_id uuid  not null,
    wight_table_id uuid  not null
);

-- Create uuid for parent
CREATE EXTENSION "pgcrypto";


--create sequence parent_seq increment 1 start 1;
create sequence baby_seq increment 1 start 1;
create sequence feed_seq increment 1 start 1;
create sequence wight_seq increment 1 start 1;
