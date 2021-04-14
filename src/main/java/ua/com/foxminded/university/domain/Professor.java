package ua.com.foxminded.university.domain;

import java.util.List;
import java.util.Objects;

public class Professor extends User{

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Faculty faculty;
    private Department department;
    private ScienceDegree scienceDegree;
    private List<Group> slavesGroups;
    private List<Discipline> disciplines;

    protected Professor(ProfessorBuilder userUserBuilder) {
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

    public Faculty getFaculty() {
        return faculty;
    }

    public Department getDepartment() {
        return department;
    }

    public ScienceDegree getScienceDegree() {
        return scienceDegree;
    }

    public List<Group> getSlavesGroups() {
        return slavesGroups;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Professor professor = (Professor) o;
        return Objects.equals(firstName, professor.firstName) &&
                Objects.equals(lastName, professor.lastName) &&
                Objects.equals(email, professor.email) &&
                Objects.equals(password, professor.password) &&
                Objects.equals(faculty, professor.faculty) &&
                Objects.equals(department, professor.department) &&
                Objects.equals(scienceDegree, professor.scienceDegree) &&
                Objects.equals(slavesGroups, professor.slavesGroups) &&
                Objects.equals(disciplines, professor.disciplines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password, faculty, department, scienceDegree, slavesGroups, disciplines);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", faculty=" + faculty +
                ", department=" + department +
                ", scienceDegree=" + scienceDegree +
                ", slavesGroups=" + slavesGroups +
                ", disciplines=" + disciplines +
                '}';
    }

    public static class ProfessorBuilder extends UserBuilder<ProfessorBuilder> {
        private Faculty faculty;
        private Department department;
        private ScienceDegree scienceDegree;
        private List<Group> slavesGroups;
        private List<Discipline> disciplines;

        public ProfessorBuilder() {
        }

        @Override
        public ProfessorBuilder self() {
            return this;
        }

        public Professor build() {
            return new Professor(self());
        }

        public ProfessorBuilder withFaculty(Faculty faculty) {
            this.faculty = faculty;
            return self();
        }

        public ProfessorBuilder withDepartment(Department department) {
            this.department = department;
            return self();
        }

        public ProfessorBuilder withScienceDegree(ScienceDegree scienceDegree) {
            this.scienceDegree = scienceDegree;
            return self();
        }

        public ProfessorBuilder withGroups(List<Group> slavesGroups) {
            this.slavesGroups = slavesGroups;
            return self();
        }

        public ProfessorBuilder withDisciplines(List<Discipline> disciplines) {
            this.disciplines = disciplines;
            return self();
        }
    }

}
