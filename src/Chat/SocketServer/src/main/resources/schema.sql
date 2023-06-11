drop table if exists user_table cascade;
drop table if exists messages cascade;
drop table if exists chat_rooms cascade;
drop table if exists user_rooms cascade;


create table if not exists user_table 
(
	id bigint generated by default as identity primary key,
	name varchar(100) unique not null,
	password varchar(100)
);

create table if not exists chat_rooms 
(
	id bigint generated by default as identity primary key,
	name varchar(500),
	creator bigint references user_table (id)
);

create table if not exists messages 
(
	id bigint generated by default as identity primary key,
	author bigint references user_table (id),
	room bigint references chat_rooms (id),
	text varchar(500),
	dateTime timestamp not null
);