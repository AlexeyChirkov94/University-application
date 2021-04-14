package ua.com.foxminded.university.domain;

import java.util.Objects;

public class Discipline {

    private final String name;

    private Discipline(Builder builder) {
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
        Discipline that = (Discipline) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Discipline{" +
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

        public Discipline build() {
            return new Discipline(this);
        }
    }

}
