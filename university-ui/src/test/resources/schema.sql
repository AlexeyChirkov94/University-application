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


create table DEPARTMENTS
(
    id serial primary key,
    name varchar(50)
);

create table COURSES
(
    id serial primary key,
    name varchar(50),
    department_id bigint references departments(id)
);

create table FORMS_OF_EDUCATION
(
    id serial primary key,
    name varchar(50)
);

create table FORMS_OF_LESSON
(
    id serial primary key,
    name varchar(50),
    duration_of_lesson int
);

create table GROUPS_OF_STUDENTS
(
    id serial primary key,
    name varchar(50),
    department_id bigint references departments(id),
    forms_of_education_id bigint references forms_of_education(id)
);

create table USERS
(
    id serial primary key,
    type varchar(30) not null ,
    first_name varchar(50),
    last_name varchar(50),
    email varchar(50),
    password varchar(60),
    group_id bigint references groups_of_students(id),
    department_id bigint references departments(id),
    scienceDegree_id int
);

create table ROLES_OF_USERS
(
    id serial primary key,
    name varchar(50)

);

create table PRIVILEGES
(
    id serial primary key,
    name varchar(50)
);

create table USER_ROLE
(
    user_id bigint references users(id),
    role_id bigint references roles_of_users(id)
);

create table ROLE_PRIVILEGE
(
    role_id bigint references roles_of_users(id),
    privilege_id bigint references privileges(id)

);

create table LESSONS
(
    id serial primary key,
    course_id bigint references courses(id),
    timeOfStart timestamp,
    group_id bigint references groups_of_students(id),
    professor_id bigint references users(id),
    forms_of_lesson_id bigint references forms_of_lesson(id)
);

create table PROFESSOR_COURSE
(
    professor_id bigint references users(id),
    course_id bigint references courses(id)
);
