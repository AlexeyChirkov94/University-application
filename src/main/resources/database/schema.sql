set timezone TO 'Europe/Moscow';
drop table if exists "professor_course" CASCADE;
drop table if exists "role_privilege" CASCADE;
drop table if exists "user_role" CASCADE;
drop table if exists "usersroles" CASCADE;
drop table if exists "privileges" CASCADE;
drop table if exists "users" CASCADE;
drop table if exists "lessons" CASCADE;
drop table if exists "formsoflesson" CASCADE;
drop table if exists "courses" CASCADE;
drop table if exists "groups" CASCADE;
drop table if exists "departments" CASCADE;
drop table if exists "formsofeducation" CASCADE;

create table departments
(
    id serial primary key unique,
    name varchar(50)
);

create table courses
(
    id serial primary key unique,
    name varchar(50),
    department_id bigint references departments(id)
);

create table formsOfEducation
(
    id serial primary key unique,
    name varchar(50)
);

create table formsOfLesson
(
    id serial primary key unique,
    name varchar(50),
    durationOfLesson int
);

create table groups
(
    id serial primary key unique,
    name varchar(50),
    department_id bigint references departments(id),
    formOfEducation_id bigint references formsOfEducation(id)
);

create table users
(
    id serial primary key unique,
    type varchar(30) not null ,
    first_name varchar(50),
    last_name varchar(50),
    email varchar(50) unique,
    password varchar(60),
    group_id bigint references groups(id),
    department_id bigint references departments(id),
    scienceDegree_id int
);

create table usersroles
(
    id serial primary key unique,
    name varchar(50)

);

create table privileges
(
    id serial primary key unique,
    name varchar(50)
);

create table user_role
(
    user_id bigint references users(id),
    role_id bigint references usersroles(id)
);

create table role_privilege
(
    role_id bigint references usersroles(id),
    privilege_id bigint references privileges(id)

);

create table lessons
(
    id serial primary key unique,
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
