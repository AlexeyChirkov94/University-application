package ua.com.foxminded.university.domain;

import java.util.Objects;

public class FormOfEducation {

    private final String name;

    private FormOfEducation(Builder builder) {
        this.name = builder.name;
    }

    public static Builder builder() {
        return new Builder();
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
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "FormOfEducation{" +
                "name='" + name + '\'' +
                '}';
    }

    public static class Builder {
        private String name;

        private Builder() {
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
