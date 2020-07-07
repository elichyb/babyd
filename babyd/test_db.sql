drop database test;
drop user pp;

create user pp with password 'admin';
create database test with template=template0 owner=pp;
\connect test;
alter default privileges grant all on tables to pp;
alter default privileges grant all on sequences to pp;

create table parent (
    parent_id integer  primary key not null,
    first_name varchar (50) not null,
    last_name varchar (50) not null,
    email varchar (50) not null,
    password text not null
);

create table baby (
    baby_id integer  primary key not null,
    first_name varchar not null,
    last_name varchar not null,
    birth_date date not null
);

create table parent_baby_role (
    parent_id integer   not null,
    baby_id integer  not null,
    role_id integer  not null
);

create sequence parent_seq increment 1 start 1;
create sequence baby_seq increment 1 start 1;
