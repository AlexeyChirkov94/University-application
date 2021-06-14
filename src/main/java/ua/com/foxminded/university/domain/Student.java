package ua.com.foxminded.university.domain;

import java.util.Objects;

public class Student extends User {

    private Group group;

    protected Student(StudentBuilder userUserBuilder) {
        super(userUserBuilder);
        this.group = userUserBuilder.group;
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public Group getGroup() {
        return group;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(group, student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), group);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + this.getId() +
                ", firstName='" + this.getFirstName() + '\'' +
                ", lastName='" + this.getLastName() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                ", group=" + group +
                '}';
    }

    public static class StudentBuilder extends UserBuilder<StudentBuilder> {
        private Group group;

        private StudentBuilder() {
        }

        @Override
        public StudentBuilder self() {
            return this;
        }

        public Student build() {
            return new Student(self());
        }

        public StudentBuilder withGroup(Group group) {
            this.group = group;
            return self();
        }

    }

}
