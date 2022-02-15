package ua.com.foxminded.university.service.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.exception.IncompatibilityCourseAndProfessorException;
import ua.com.foxminded.university.service.exception.ValidateException;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class LessonValidatorImpl implements LessonValidator{

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Override
    public void validateCompatibilityCourseAndProfessor(long courseId, long professorId){
        if(courseId != 0 && professorId != 0) {
            List<Course> professorCourses = courseRepository.findByProfessorId(professorId);
            boolean canProfessorTeachThisCourse =
                    professorCourses.stream()
                            .map(Course::getId)
                            .collect(Collectors.toList())
                            .contains(courseId);

            if (!canProfessorTeachThisCourse) {
                throw new IncompatibilityCourseAndProfessorException("Selected professor can`t teach this course");
            }
        }
    }

    @Override
    public void checkGroupTimeTableCrossing(long lessonId, long groupId) {
        List<Lesson> existingLessonsOfGroup = lessonRepository.findAllByGroupIdOrderByTimeOfStartLesson(groupId);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new EntityDontExistException("There are no lesson with id: " + lessonId));
        existingLessonsOfGroup.remove(lesson);
        if(lesson.getTimeOfStartLesson() != null) {
            boolean isNewLessonMatchesWithAnotherLessonsInGroup = existingLessonsOfGroup.stream()
                    .map(Lesson::getTimeOfStartLesson)
                    .collect(Collectors.toList()).contains(lesson.getTimeOfStartLesson());
            if (isNewLessonMatchesWithAnotherLessonsInGroup) {
                throw new ValidateException("Lesson can`t be appointed on this time, cause time of lesson cross timetable of group");
            }
        }
    }

    @Override
    public void checkProfessorTimeTableCrossing(long lessonId, long professorId) {
        List<Lesson> existingLessonOfTeacher = lessonRepository.findAllByTeacherIdOrderByTimeOfStartLesson(professorId);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new EntityDontExistException("There are no lesson with id: " + lessonId));
        existingLessonOfTeacher.remove(lesson);
        if(lesson.getTimeOfStartLesson() != null) {
            boolean isNewLessonMatchesWithAnotherLessonsOfProfessor = existingLessonOfTeacher.stream()
                    .map(Lesson::getTimeOfStartLesson)
                    .collect(Collectors.toList()).contains(lesson.getTimeOfStartLesson());
            if (isNewLessonMatchesWithAnotherLessonsOfProfessor) {
                throw new ValidateException("Lesson can`t be appointed on this time, cause time of lesson cross timetable of professor");
            }
        }
    }

}
