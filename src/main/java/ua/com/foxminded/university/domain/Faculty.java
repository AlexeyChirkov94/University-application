package ua.com.foxminded.university.domain;

import java.util.List;
import java.util.Objects;

public class Faculty {

    private final String name;
    private final List<Department> departments;

    private Faculty(Builder builder) {
        this.name = builder.name;
        this.departments = builder.departments;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public List<Department> getDepartments() {
        return departments;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Faculty faculty = (Faculty) o;
        return Objects.equals(name, faculty.name) &&
                Objects.equals(departments, faculty.departments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, departments);
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "name='" + name + '\'' +
                ", departments=" + departments +
                '}';
    }

    public static class Builder {
        private String name;
        private List<Department> departments;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDepartments(List<Department> departments) {
            this.departments = departments;
            return this;
        }

        public Faculty build() {
            return new Faculty(this);
        }
    }

}
