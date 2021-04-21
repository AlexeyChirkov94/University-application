package ua.com.foxminded.university.domain;

import java.util.List;
import java.util.Objects;

public class Department {

    private final String name;
    private final List<Professor> professors;
    private final List<Group> groups;
    private final List<Discipline> disciplines;

    private Department(Builder builder){
        this.name = builder.name;
        this.professors = builder.professors;
        this.groups = builder.groups;
        this.disciplines = builder.disciplines;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public List<Group> getGroups() {
        return groups;
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
        Department that = (Department) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(professors, that.professors) &&
                Objects.equals(groups, that.groups) &&
                Objects.equals(disciplines, that.disciplines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, professors, groups, disciplines);
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", professors=" + professors +
                ", groups=" + groups +
                ", disciplines=" + disciplines +
                '}';
    }

    public static class Builder {
        private String name;
        private List<Professor> professors;
        private List<Group> groups;
        private List<Discipline> disciplines;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withProfessors(List<Professor> professors) {
            this.professors = professors;
            return this;
        }

        public Builder withGroups(List<Group> groups) {
            this.groups = groups;
            return this;
        }

        public Builder withDisciplines(List<Discipline> disciplines) {
            this.disciplines = disciplines;
            return this;
        }

        public Department build() {
            return new Department(this);
        }
    }

}
