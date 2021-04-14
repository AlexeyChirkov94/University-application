package ua.com.foxminded.university.domain;

import java.util.List;
import java.util.Objects;

public class University {

    private final String name;
    private final List<Faculty> faculties;

    private University(Builder builder) {
        this.name = builder.name;
        this.faculties = builder.faculties;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        University that = (University) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(faculties, that.faculties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, faculties);
    }

    @Override
    public String toString() {
        return "University{" +
                "name='" + name + '\'' +
                ", faculties=" + faculties +
                '}';
    }

    public static class Builder {
        private String name;
        private List<Faculty> faculties;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withFaculties(List<Faculty> faculties) {
            this.faculties = faculties;
            return this;
        }

        public University build() {
            return new University(this);
        }
    }

}
