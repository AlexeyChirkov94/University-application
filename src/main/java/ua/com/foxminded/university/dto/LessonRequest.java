package ua.com.foxminded.university.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class LessonRequest {

    private Long id;
    private CourseRequest courseRequest;
    private LocalDateTime timeOfStartLesson;
    private GroupRequest groupRequest;
    private ProfessorRequest teacher;
    private FormOfLessonRequest formOfLessonRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseRequest getCourseRequest() {
        return courseRequest;
    }

    public void setCourseRequest(CourseRequest courseRequest) {
        this.courseRequest = courseRequest;
    }

    public LocalDateTime getTimeOfStartLesson() {
        return timeOfStartLesson;
    }

    public void setTimeOfStartLesson(LocalDateTime timeOfStartLesson) {
        this.timeOfStartLesson = timeOfStartLesson;
    }

    public GroupRequest getGroupRequest() {
        return groupRequest;
    }

    public void setGroupRequest(GroupRequest groupRequest) {
        this.groupRequest = groupRequest;
    }

    public ProfessorRequest getTeacher() {
        return teacher;
    }

    public void setTeacher(ProfessorRequest teacher) {
        this.teacher = teacher;
    }

    public FormOfLessonRequest getFormOfLessonRequest() {
        return formOfLessonRequest;
    }

    public void setFormOfLessonRequest(FormOfLessonRequest formOfLessonRequest) {
        this.formOfLessonRequest = formOfLessonRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LessonRequest lessonRequest = (LessonRequest) o;
        return Objects.equals(id, lessonRequest.id) &&
                Objects.equals(courseRequest, lessonRequest.courseRequest) &&
                Objects.equals(timeOfStartLesson, lessonRequest.timeOfStartLesson) &&
                Objects.equals(groupRequest, lessonRequest.groupRequest) &&
                Objects.equals(teacher, lessonRequest.teacher) &&
                Objects.equals(formOfLessonRequest, lessonRequest.formOfLessonRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseRequest, timeOfStartLesson, groupRequest, teacher, formOfLessonRequest);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", discipline=" + courseRequest +
                ", timeOfStartLesson=" + timeOfStartLesson +
                ", group=" + groupRequest +
                ", teacher=" + teacher +
                ", formOfLesson=" + formOfLessonRequest +
                '}';
    }

}
