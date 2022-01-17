package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.dto.FormOfLessonResponse;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.dto.ScienceDegreeResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Professor;

import static org.assertj.core.api.Assertions.assertThat;

class LessonMapperTest {

    ApplicationContext context;
    LessonMapper lessonMapper;
    Lesson lesson;
    Lesson lessonWithEntities;
    Lesson emptyLesson;
    LessonResponse lessonResponse;
    LessonRequest lessonRequest;
    LessonResponse emptyLessonResponse;

    FormOfLessonResponse formOfLessonResponse;
    FormOfLessonResponse emptyFormOfLessonResponse;
    ProfessorResponse professorResponse;
    ProfessorResponse emptyProfessorResponse;
    CourseResponse courseResponse;
    CourseResponse emptyCourseResponse;
    GroupResponse groupResponse;
    GroupResponse emptyGroupResponse;
    DepartmentResponse departmentResponse;
    FormOfEducationResponse formOfEducationResponse;

    {
        context = new AnnotationConfigApplicationContext("ua.com.foxminded.university.mapper");
        lessonMapper = context.getBean(LessonMapper.class);

        lesson = Lesson.builder().withId(1L).build();
        emptyLesson = Lesson.builder().build();
        lessonWithEntities = Lesson.builder().withId(1L)
                .withFormOfLesson(FormOfLesson.builder().withId(1L).withName("Form 1").build())
                .withTeacher(Professor.builder().withId(1L).withFirstName("Alex").build())
                .withCourse(Course.builder().withId(1L).withName("Course 1").build())
                .withGroup(Group.builder().withId(1L).withName("Group 1").build())
                .build();

        lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);

        departmentResponse = new DepartmentResponse();
        departmentResponse.setId(0L);
        departmentResponse.setName("");
        formOfEducationResponse = new FormOfEducationResponse();
        formOfEducationResponse.setId(0L);
        formOfEducationResponse.setName("");

        formOfLessonResponse = new FormOfLessonResponse();
        formOfLessonResponse.setId(1L);
        formOfLessonResponse.setName("Form 1");
        formOfLessonResponse.setDuration(0);
        emptyFormOfLessonResponse = new FormOfLessonResponse();
        emptyFormOfLessonResponse.setId(0L);
        emptyFormOfLessonResponse.setName("");

        professorResponse = new ProfessorResponse();
        professorResponse.setId(1L);
        professorResponse.setFirstName("Alex");
        professorResponse.setLastName("");
        professorResponse.setEmail("");
        professorResponse.setPassword("");
        professorResponse.setDepartmentResponse(departmentResponse);
        professorResponse.setScienceDegreeResponse(ScienceDegreeResponse.GRADUATE);
        emptyProfessorResponse = new ProfessorResponse();
        emptyProfessorResponse.setId(0L);

        courseResponse = new CourseResponse();
        courseResponse.setId(1L);
        courseResponse.setName("Course 1");
        courseResponse.setDepartmentResponse(departmentResponse);
        emptyCourseResponse = new CourseResponse();
        emptyCourseResponse.setId(0L);
        emptyCourseResponse.setName("");

        groupResponse = new GroupResponse();
        groupResponse.setId(1L);
        groupResponse.setName("Group 1");
        groupResponse.setDepartmentResponse(departmentResponse);
        groupResponse.setFormOfEducationResponse(formOfEducationResponse);
        emptyGroupResponse = new GroupResponse();
        emptyGroupResponse.setId(0L);
        emptyGroupResponse.setName("");

        lessonResponse = new LessonResponse();
        lessonResponse.setId(1L);
        lessonResponse.setTeacher(professorResponse);
        lessonResponse.setCourseResponse(courseResponse);
        lessonResponse.setFormOfLessonResponse(formOfLessonResponse);
        lessonResponse.setGroupResponse(groupResponse);

        emptyLessonResponse = new LessonResponse();
        emptyLessonResponse.setId(0L);
        emptyLessonResponse.setTeacher(emptyProfessorResponse);
        emptyLessonResponse.setCourseResponse(emptyCourseResponse);
        emptyLessonResponse.setFormOfLessonResponse(emptyFormOfLessonResponse);
        emptyLessonResponse.setGroupResponse(emptyGroupResponse);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsLessonEntity() {
        LessonResponse expected = lessonResponse;
        LessonResponse actual = lessonMapper.mapEntityToDto(lessonWithEntities);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsLessonEntityWhichNotFoundedInDB() {
        LessonResponse expected = emptyLessonResponse;
        LessonResponse actual = lessonMapper.mapEntityToDto(emptyLesson);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(lessonMapper.mapEntityToDto(null)).isNull();
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsLessonRequestDto() {
        Lesson expected = lesson;
        Lesson actual = lessonMapper.mapDtoToEntity(lessonRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(lessonMapper.mapDtoToEntity(null)).isNull();
    }

}
