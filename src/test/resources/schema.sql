drop table if exists "PROFESSOR_COURSE" CASCADE;
drop table if exists "USERS" CASCADE;
drop table if exists "LESSONS" CASCADE;
drop table if exists "FORMSOFLESSON" CASCADE;
drop table if exists "COURSES" CASCADE;
drop table if exists "GROUPS" CASCADE;
drop table if exists "DEPARTMENTS" CASCADE;
drop table if exists "SCIENCEDEFREES"CASCADE ;
drop table if exists "FORMSOFEDUCATION" CASCADE;


create table DEPARTMENTS
(
    id serial primary key,
    name varchar(30)
);

create table courses
(
    id serial primary key,
    name varchar(30),
    department_id bigint references DEPARTMENTS(id)
);

create table formsOfEducation
(
    id serial primary key,
    name varchar(30)
);

create table formsOfLesson
(
    id serial primary key,
    name varchar(30),
    durationOfLesson int
);

create table groups
(
    id serial primary key,
    name varchar(30),
    department_id bigint references departments(id),
    formOfEducation_id bigint references formsOfEducation(id)
);

create table users
(
    id serial primary key,
    type varchar(30) not null ,
    first_name varchar(30),
    last_name varchar(30),
    email varchar(50),
    password varchar(30),
    group_id bigint references groups(id),
    department_id bigint references departments(id),
    scienceDegree_id int

);

create table lessons
(
    id serial primary key,
    course_id bigint references courses(id),
    timeOfStart timestamp,
    group_id bigint references groups(id),
    professor_id bigint references users(id),
    formOfLesson_id bigint references formsOfLesson(id)
);

create table professor_course
(
    professor_id bigint references users(id),
    course_id bigint references courses(id)
);

