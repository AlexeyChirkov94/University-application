insert into departments (name) values ('Department of History'), ('Department of Mathematics');
insert into formsOfEducation (name) values ('full-time'), ('correspondence') ,('distance') ,('evening');
insert into groups (name, department_id, formOfEducation_id) values ('History Group №1', 1, 1);
insert into groups (name, department_id, formOfEducation_id) values ('History Group №2', 1, 2);
insert into formsOfLesson (name, durationOfLesson) values ('lecture', 60), ('practice', 120);

insert into courses (name, department_id)
values ('Russia History' ,1),
('Italic History', 1),
('Germany History', 1),
('France History', 1);

INSERT INTO users (type, first_name, last_name, email, password, group_id)
VALUES ('student', 'Alexey', 'Chrikov', 'chrikov@gmail.com', '1234', 1),
('student', 'Vasiliy', 'Osipov', 'osipov@gmail.com', '1234', 1),
('student', 'Nikita', 'Grigirev', 'grigorev@gmail.com', '1234', 1),
('student', 'Daria', 'Zueva', 'zueva@gmail.com', '1234', 1),
('student', 'Maksim', 'Panichev', 'panichev@gmail.com', '1234', 2),
('student', 'Petr', 'Elematov', 'elematov@gmail.com', '1234', 2);

INSERT INTO users (type, first_name, last_name, email, password, department_id, scienceDegree_id)
VALUES('professor', 'Ivan', 'Petrov', 'petrov@gmail.com', 'RI', 1, 1),
('professor', 'Ivan', 'Fedorv', 'fedorv@gmail.com', 'IGF', 1, 2),
('professor', 'Ivan', 'Seregin', 'seregin@gmail.com', 'RIGF', 1, 3),
('professor', 'Ivan', 'Mazurin', 'Mazurin@gmail.com', '1234', 1, 1);

insert into professor_course (professor_id, course_id)
values (7,1), (7,2),
(8,2), (8,3), (8,4),
(9,1), (9,2), (9,3), (9,4);

insert into lessons (course_id, timeOfStart, group_id, professor_id, formOfLesson_id)
values (1, '2020-01-11 10:00:00-00', 1, 7, 1),
(1, '2020-01-11 12:00:00-00', 1, 7, 2),
(3, '2020-01-11 14:00:00-00', 1, 8, 1),
(4, '2020-01-11 18:00:00-00', 2, 9, 1),
(4, '2020-01-11 20:00:00-00', 2, 9, 2);