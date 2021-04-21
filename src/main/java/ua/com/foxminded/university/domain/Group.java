package ua.com.foxminded.university.domain;

import java.util.List;
import java.util.Objects;

public class Group {

    private final String name;
    private final Faculty faculty;
    private final Department department;
    private final FormOfEducation formOfEducation;
    private final TimeTable timeTable;
    private final List<Professor> teachers;
    private final List<Student> students;

    private Group(Builder builder){
        this.name = builder.name;
        this.faculty = builder.faculty;
        this.department = builder.department;
        this.formOfEducation = builder.formOfEducation;
        this.timeTable = builder.timeTable;
        this.teachers = builder.teachers;
        this.students = builder.students;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public Department getDepartment() {
        return department;
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Professor> getTeachers() {
        return teachers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group = (Group) o;
        return Objects.equals(name, group.name) &&
                Objects.equals(faculty, group.faculty) &&
                Objects.equals(department, group.department) &&
                Objects.equals(formOfEducation, group.formOfEducation) &&
                Objects.equals(timeTable, group.timeTable) &&
                Objects.equals(teachers, group.teachers) &&
                Objects.equals(students, group.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, faculty, department, formOfEducation, timeTable, teachers, students);
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", faculty=" + faculty +
                ", department=" + department +
                ", formOfEducation=" + formOfEducation +
                ", timeTable=" + timeTable +
                ", teachers=" + teachers +
                ", students=" + students +
                '}';
    }

    public static class Builder {
        private String name;
        private Faculty faculty;
        private Department department;
        private FormOfEducation formOfEducation;
        private TimeTable timeTable;
        private List<Professor> teachers;
        private List<Student> students;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withFaculty(Faculty faculty) {
            this.faculty = faculty;
            return this;
        }

        public Builder withDepartment(Department department) {
            this.department = department;
            return this;
        }

        public Builder withFormOfEducation(FormOfEducation formOfEducation) {
            this.formOfEducation = formOfEducation;
            return this;
        }

        public Builder withTimeTable(TimeTable timeTable) {
            this.timeTable = timeTable;
            return this;
        }

        public Builder withTeachers(List<Professor> teachers) {
            this.teachers = teachers;
            return this;
        }

        public Builder withStudents(List<Student> students) {
            this.students = students;
            return this;
        }

        public Group build() {
            return new Group(this);
        }
    }

}
