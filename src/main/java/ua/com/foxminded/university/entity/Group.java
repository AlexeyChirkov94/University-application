package ua.com.foxminded.university.entity;

import java.util.Objects;

public class Group {

    private final Long id;
    private final String name;
    private final Department department;
    private final FormOfEducation formOfEducation;

    private Group(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.department = builder.department;
        this.formOfEducation = builder.formOfEducation;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Department getDepartment() {
        return department;
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
        Group group = (Group) o;
        return Objects.equals(id, group.id) &&
                Objects.equals(name, group.name) &&
                Objects.equals(department, group.department) &&
                Objects.equals(formOfEducation, group.formOfEducation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, department, formOfEducation);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department=" + department +
                ", formOfEducation=" + formOfEducation +
                '}';
    }

    public static class Builder {
        private Long id;
        private String name;
        private Department department;
        private FormOfEducation formOfEducation;

        private Builder() {
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
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

        public Group build() {
            return new Group(this);
        }
    }

}
