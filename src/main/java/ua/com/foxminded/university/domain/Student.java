package ua.com.foxminded.university.domain;

import java.util.Objects;

public class Student extends User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Integer course;
    private Faculty faculty;
    private Department department;
    private Group group;
    private FormOfEducation formOfEducation;

    protected Student(StudentBuilder userUserBuilder) {
        super(userUserBuilder);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Integer getCourse() {
        return course;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public Department getDepartment() {
        return department;
    }

    public Group getGroup() {
        return group;
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(firstName, student.firstName) &&
                Objects.equals(lastName, student.lastName) &&
                Objects.equals(email, student.email) &&
                Objects.equals(password, student.password) &&
                Objects.equals(course, student.course) &&
                Objects.equals(faculty, student.faculty) &&
                Objects.equals(department, student.department) &&
                Objects.equals(group, student.group) &&
                Objects.equals(formOfEducation, student.formOfEducation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password, course, faculty, department, group, formOfEducation);
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", course=" + course +
                ", faculty=" + faculty +
                ", department=" + department +
                ", group=" + group +
                ", formOfEducation=" + formOfEducation +
                '}';
    }

    public static class StudentBuilder extends UserBuilder<StudentBuilder> {
        private Integer course;
        private Faculty faculty;
        private Department department;
        private Group group;
        private FormOfEducation formOfEducation;

        public StudentBuilder() {
        }

        @Override
        public StudentBuilder self() {
            return this;
        }

        public Student build() {
            return new Student(self());
        }

        public StudentBuilder withCourse(Integer course) {
            this.course = course;
            return self();
        }

        public StudentBuilder withFaculty(Faculty faculty) {
            this.faculty = faculty;
            return self();
        }

        public StudentBuilder withDepartment(Department department) {
            this.department = department;
            return self();
        }

        public StudentBuilder withGroup(Group group) {
            this.group = group;
            return self();
        }

        public StudentBuilder withFormOfEducation(FormOfEducation formOfEducation) {
            this.formOfEducation = formOfEducation;
            return self();
        }
    }

}
