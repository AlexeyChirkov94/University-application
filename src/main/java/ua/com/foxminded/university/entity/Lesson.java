package ua.com.foxminded.university.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Lesson {

    private final Long id;
    private final Course course;
    private final LocalDateTime timeOfStartLesson;
    private final Group group;
    private final Professor teacher;
    private final FormOfLesson formOfLesson;

    private Lesson(Builder builder) {
        this.id = builder.id;
        this.course = builder.course;
        this.timeOfStartLesson = builder.timeOfStartLesson;
        this.group = builder.group;
        this.teacher = builder.teacher;
        this.formOfLesson = builder.formOfLesson;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDateTime getTimeOfStartLesson() {
        return timeOfStartLesson;
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
        return Objects.equals(id, lesson.id) &&
                Objects.equals(course, lesson.course) &&
                Objects.equals(timeOfStartLesson, lesson.timeOfStartLesson) &&
                Objects.equals(group, lesson.group) &&
                Objects.equals(teacher, lesson.teacher) &&
                Objects.equals(formOfLesson, lesson.formOfLesson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, course, timeOfStartLesson, group, teacher, formOfLesson);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", discipline=" + course +
                ", timeOfStartLesson=" + timeOfStartLesson +
                ", group=" + group +
                ", teacher=" + teacher +
                ", formOfLesson=" + formOfLesson +
                '}';
    }

    public static class Builder {
        private Long id;
        private Course course;
        private LocalDateTime timeOfStartLesson;
        private Group group;
        private Professor teacher;
        private FormOfLesson formOfLesson;

        private Builder() {
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCourse(Course course) {
            this.course = course;
            return this;
        }

        public Builder withTimeOfStartLesson(LocalDateTime timeOfStartLesson) {
            this.timeOfStartLesson = timeOfStartLesson;
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
