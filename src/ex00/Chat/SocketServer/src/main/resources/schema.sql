drop table if exists user_table cascade;

create table if not exists user_table 
(
id bigint generated by default as identity primary key,
name varchar(100) unique not null,
password varchar(100)
);