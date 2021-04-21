package ua.com.foxminded.university.domain;

import java.time.Duration;
import java.util.Objects;

public class FormOfLesson {

    private final String name;
    private final Duration duration;

    private FormOfLesson(Builder builder) {
        this.name = builder.name;
        this.duration = builder.duration;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public Duration getDuration() {
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
        return Objects.equals(name, that.name) &&
                Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, duration);
    }

    @Override
    public String toString() {
        return "FormOfLesson{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }

    public static class Builder {
        private String name;
        private Duration duration;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDuration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public FormOfLesson build() {
            return new FormOfLesson(this);
        }
    }

}
