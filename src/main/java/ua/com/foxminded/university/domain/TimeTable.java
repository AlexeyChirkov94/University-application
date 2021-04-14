package ua.com.foxminded.university.domain;

import java.util.List;
import java.util.Objects;

public class TimeTable {

    private final List<Lesson> lessons;

    private TimeTable(Builder builder) {
        this.lessons = builder.lessons;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeTable timeTable = (TimeTable) o;
        return Objects.equals(lessons, timeTable.lessons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessons);
    }

    @Override
    public String toString() {
        return "TimeTable{" +
                "lessons=" + lessons +
                '}';
    }

    public static class Builder {
        private List<Lesson> lessons;

        private Builder() {
        }

        public Builder withLessons(List<Lesson> lessons) {
            this.lessons = lessons;
            return this;
        }

        public TimeTable build() {
            return new TimeTable(this);
        }
    }

}
