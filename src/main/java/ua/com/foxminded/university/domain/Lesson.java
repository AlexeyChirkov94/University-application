package ua.com.foxminded.university.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Lesson {

    private final Discipline discipline;
    private final LocalDateTime timeOfStartLesson;
    private final LocalDateTime timeOfEndLesson;
    private final Group group;
    private final Professor teacher;
    private final FormOfLesson formOfLesson;

    private Lesson(Builder builder) {
        this.discipline = builder.discipline;
        this.timeOfStartLesson = builder.timeOfStartLesson;
        this.timeOfEndLesson = builder.timeOfEndLesson;
        this.group = builder.group;
        this.teacher = builder.teacher;
        this.formOfLesson = builder.formOfLesson;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public LocalDateTime getTimeOfStartLesson() {
        return timeOfStartLesson;
    }

    public LocalDateTime getTimeOfEndLesson() {
        return timeOfEndLesson;
    }

    public Group getGroup() {
        return group;
    }

    public Professor getTeacher() {
        return teacher;
    }

    public FormOfLesson getFormOfLesson() {
        return formOfLesson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lesson lesson = (Lesson) o;
        return Objects.equals(discipline, lesson.discipline) &&
                Objects.equals(timeOfStartLesson, lesson.timeOfStartLesson) &&
                Objects.equals(timeOfEndLesson, lesson.timeOfEndLesson) &&
                Objects.equals(group, lesson.group) &&
                Objects.equals(teacher, lesson.teacher) &&
                Objects.equals(formOfLesson, lesson.formOfLesson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discipline, timeOfStartLesson, timeOfEndLesson, group, teacher, formOfLesson);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "discipline=" + discipline +
                ", timeOfStartLesson=" + timeOfStartLesson +
                ", timeOfEndLesson=" + timeOfEndLesson +
                ", group=" + group +
                ", teacher=" + teacher +
                ", formOfLesson=" + formOfLesson +
                '}';
    }

    public static class Builder {
        private Discipline discipline;
        private LocalDateTime timeOfStartLesson;
        private LocalDateTime timeOfEndLesson;
        private Group group;
        private Professor teacher;
        private FormOfLesson formOfLesson;

        private Builder() {
        }

        public Builder withDiscipline(Discipline discipline) {
            this.discipline = discipline;
            return this;
        }

        public Builder withTimeOfStartLesson(LocalDateTime timeOfStartLesson) {
            this.timeOfStartLesson = timeOfStartLesson;
            return this;
        }

        public Builder withTimeOfEndLesson(LocalDateTime timeOfEndLesson) {
            this.timeOfEndLesson = timeOfEndLesson;
            return this;
        }

        public Builder withGroup(Group group) {
            this.group = group;
            return this;
        }

        public Builder withTeacher(Professor teacher) {
            this.teacher = teacher;
            return this;
        }

        public Builder withFormOfLesson(FormOfLesson formOfLesson) {
            this.formOfLesson = formOfLesson;
            return this;
        }

        public Lesson build() {
            return new Lesson(this);
        }
    }

}
