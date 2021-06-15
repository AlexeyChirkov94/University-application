package ua.com.foxminded.university.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class LessonResponse {

    private Long id;
    private CourseResponse courseResponse;
    private LocalDateTime timeOfStartLesson;
    private GroupResponse groupResponse;
    private ProfessorResponse teacher;
    private FormOfLessonResponse formOfLessonResponse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseResponse getCourseResponse() {
        return courseResponse;
    }

    public void setCourseResponse(CourseResponse courseResponse) {
        this.courseResponse = courseResponse;
    }

    public LocalDateTime getTimeOfStartLesson() {
        return timeOfStartLesson;
    }

    public void setTimeOfStartLesson(LocalDateTime timeOfStartLesson) {
        this.timeOfStartLesson = timeOfStartLesson;
    }

    public GroupResponse getGroupResponse() {
        return groupResponse;
    }

    public void setGroupResponse(GroupResponse groupResponse) {
        this.groupResponse = groupResponse;
    }

    public ProfessorResponse getTeacher() {
        return teacher;
    }

    public void setTeacher(ProfessorResponse teacher) {
        this.teacher = teacher;
    }

    public FormOfLessonResponse getFormOfLessonResponse() {
        return formOfLessonResponse;
    }

    public void setFormOfLessonResponse(FormOfLessonResponse formOfLessonResponse) {
        this.formOfLessonResponse = formOfLessonResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LessonResponse lessonResponse = (LessonResponse) o;
        return Objects.equals(id, lessonResponse.id) &&
                Objects.equals(courseResponse, lessonResponse.courseResponse) &&
                Objects.equals(timeOfStartLesson, lessonResponse.timeOfStartLesson) &&
                Objects.equals(groupResponse, lessonResponse.groupResponse) &&
                Objects.equals(teacher, lessonResponse.teacher) &&
                Objects.equals(formOfLessonResponse, lessonResponse.formOfLessonResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseResponse, timeOfStartLesson, groupResponse, teacher, formOfLessonResponse);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", discipline=" + courseResponse +
                ", timeOfStartLesson=" + timeOfStartLesson +
                ", group=" + groupResponse +
                ", teacher=" + teacher +
                ", formOfLesson=" + formOfLessonResponse +
                '}';
    }

}
