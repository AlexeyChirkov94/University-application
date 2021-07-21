drop table if exists "PROFESSOR_COURSE" CASCADE;
drop table if exists "USERS" CASCADE;
drop table if exists "LESSONS" CASCADE;
drop table if exists "FORMSOFLESSON" CASCADE;
drop table if exists "COURSES" CASCADE;
drop table if exists "GROUPS" CASCADE;
drop table if exists "DEPARTMENTS" CASCADE;
drop table if exists "SCIENCEDEFREES"CASCADE;
drop table if exists "FORMSOFEDUCATION" CASCADE;


create table DEPARTMENTS
(
    id serial primary key,
    name varchar(30)
);

create table COURSES
(
    id serial primary key,
    name varchar(30),
    department_id bigint references DEPARTMENTS(id)
);

create table FORMSOFEDUCATION
(
    id serial primary key,
    name varchar(30)
);

create table FORMSOFLESSON
(
    id serial primary key,
    name varchar(30),
    durationOfLesson int
);

create table GROUPS
(
    id serial primary key,
    name varchar(30),
    department_id bigint references DEPARTMENTS(id),
    formOfEducation_id bigint references FORMSOFEDUCATION(id)
);

create table USERS
(
    id serial primary key,
    type varchar(30) not null ,
    first_name varchar(30),
    last_name varchar(30),
    email varchar(50),
    password varchar(60),
    group_id bigint references GROUPS(id),
    department_id bigint references DEPARTMENTS(id),
    scienceDegree_id int

);

create table LESSONS
(
    id serial primary key,
    course_id bigint references COURSES(id),
    timeOfStart timestamp,
    group_id bigint references GROUPS(id),
    professor_id bigint references USERS(id),
    formOfLesson_id bigint references FORMSOFLESSON(id)
);

create table PROFESSOR_COURSE
(
    professor_id bigint references USERS(id),
    course_id bigint references COURSES(id)
);

