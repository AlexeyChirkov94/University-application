package ua.com.foxminded.university.domain;

import java.util.Objects;

public class FormOfEducation {

    private final Long id;
    private final String name;

    private FormOfEducation(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormOfEducation that = (FormOfEducation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "FormOfEducation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static class Builder {
        private Long id;
        private String name;

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

        public FormOfEducation build() {
            return new FormOfEducation(this);
        }
    }
}
