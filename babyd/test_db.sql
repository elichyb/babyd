drop database test;

create database test;

\connect test;

create table parent (
    parent_id uuid   primary key not null,
    first_name varchar (50) not null,
    last_name varchar (50) not null,
    email varchar (50) not null,
    phone varchar (50) not null,
    password text not null
);

create table baby (
    baby_id uuid  primary key not null,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    birth_day varchar(50) not null,
    food_type integer not null
);

create table parent_baby_relation (
    parent_id uuid not null,
    baby_id uuid  not null,
    CONSTRAINT P_B_KEY primary key (parent_id, baby_id)
);

