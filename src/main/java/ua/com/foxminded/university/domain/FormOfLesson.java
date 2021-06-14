package ua.com.foxminded.university.domain;

import java.util.Objects;

public class FormOfLesson {

    private final Long id;
    private final String name;
    private final Integer duration;

    private FormOfLesson(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.duration = builder.duration;
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

    public Integer getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormOfLesson that = (FormOfLesson) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, duration);
    }

    @Override
    public String toString() {
        return "FormOfLesson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }

    public static class Builder {
        private Long id;
        private String name;
        private Integer duration;

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

        public Builder withDuration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public FormOfLesson build() {
            return new FormOfLesson(this);
        }
    }

}
