package ua.com.foxminded.university.domain;

import java.util.List;
import java.util.Objects;

public class Professor extends User{

    private Department department;
    private ScienceDegree scienceDegree;
    private List<Course> courses;

    protected Professor(ProfessorBuilder userUserBuilder) {
        super(userUserBuilder);
        this.department = userUserBuilder.department;
        this.scienceDegree = userUserBuilder.scienceDegree;
        this.courses = userUserBuilder.courses;
    }

    public static ProfessorBuilder builder() {
        return new ProfessorBuilder();
    }

    public Department getDepartment() {
        return department;
    }

    public ScienceDegree getScienceDegree() {
        return scienceDegree;
    }

    public List<Course> getCourses() {
        return courses;
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
        Professor professor = (Professor) o;
        return Objects.equals(department, professor.department) &&
                Objects.equals(scienceDegree, professor.scienceDegree) &&
                Objects.equals(courses, professor.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), department, scienceDegree, courses);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + this.getId() +
                ", firstName='" + this.getFirstName() + '\'' +
                ", lastName='" + this.getLastName() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                ", department=" + department +
                ", scienceDegree=" + scienceDegree +
                ", disciplines=" + courses +
                '}';
    }

    public static class ProfessorBuilder extends UserBuilder<ProfessorBuilder> {
        private Department department;
        private ScienceDegree scienceDegree;
        private List<Course> courses;

        private ProfessorBuilder() {
        }

        @Override
        public ProfessorBuilder self() {
            return this;
        }

        public Professor build() {
            return new Professor(self());
        }

        public ProfessorBuilder withDepartment(Department department) {
            this.department = department;
            return self();
        }

        public ProfessorBuilder withScienceDegree(ScienceDegree scienceDegree) {
            this.scienceDegree = scienceDegree;
            return self();
        }

        public ProfessorBuilder withCourses(List<Course> courses) {
            this.courses = courses;
            return self();
        }
    }

}
