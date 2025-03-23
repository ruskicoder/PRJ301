create database prj_workshop1_se192357
use prj_workshop1_se192357
go

create table tblUsers(
Username varchar(50) primary key,
Name varchar(100) not null,
Password varchar(255) not null,
Role varchar(20) not null)

create table tblStartupProjects(
project_id int primary key,
project_name varchar(100) not null,
description text,
Status varchar(20) not null,
estimated_launch date not null

)

                                          