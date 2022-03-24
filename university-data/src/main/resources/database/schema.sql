set timezone TO 'Europe/Moscow';
drop table if exists professor_course CASCADE;
drop table if exists role_privilege CASCADE;
drop table if exists user_role CASCADE;
drop table if exists roles_of_users CASCADE;
drop table if exists privileges CASCADE;
drop table if exists users CASCADE;
drop table if exists lessons CASCADE;
drop table if exists forms_of_lesson CASCADE;
drop table if exists courses CASCADE;
drop table if exists groups_of_students CASCADE;
drop table if exists departments CASCADE;
drop table if exists forms_of_education CASCADE;

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

create table forms_of_education
(
    id serial primary key unique,
    name varchar(50)
);

create table forms_of_lesson
(
    id serial primary key unique,
    name varchar(50),
    duration_of_lesson int
);

create table groups_of_students
(
    id serial primary key unique,
    name varchar(50),
    department_id bigint references departments(id),
    forms_of_education_id bigint references forms_of_education(id)
);

create table users
(
    id serial primary key unique,
    type varchar(30) not null ,
    first_name varchar(50),
    last_name varchar(50),
    email varchar(50) unique,
    password varchar(60),
    group_id bigint references groups_of_students(id),
    department_id bigint references departments(id),
    scienceDegree_id int
);

create table roles_of_users
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
    role_id bigint references roles_of_users(id)
);

create table role_privilege
(
    role_id bigint references roles_of_users(id),
    privilege_id bigint references privileges(id)

);

create table lessons
(
    id serial primary key unique,
    course_id bigint references courses(id),
    timeOfStart timestamp,
    group_id bigint references groups_of_students(id),
    professor_id bigint references users(id),
    forms_of_lesson_id bigint references forms_of_lesson(id)
);

create table professor_course
(
    professor_id bigint references users(id),
    course_id bigint references courses(id)
);
