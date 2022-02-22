package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.entity.Lesson;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findAllByGroupIdOrderByTimeOfStartLesson(long groupId);

    List<Lesson> findAllByTeacherIdOrderByTimeOfStartLesson(long professorId);

    List<Lesson> findAllByCourseIdOrderByTimeOfStartLesson(long courseId);

    List<Lesson> findAllByFormOfLessonIdOrderByTimeOfStartLesson(long formOfLessonId);

    @Transactional
    @Modifying
    @Query("UPDATE Lesson l SET l.formOfLesson.id =:newFormOfLessonId WHERE l.id =:lessonId")
    void changeFormOfLesson(long lessonId, long newFormOfLessonId);

    @Transactional
    @Modifying
    @Query("UPDATE Lesson l SET l.formOfLesson.id = null WHERE l.id =:lessonId")
    void removeFormOfLessonFromLesson(long lessonId);

    @Transactional
    @Modifying
    @Query("UPDATE Lesson l SET l.teacher.id =:newProfessorId WHERE l.id =:lessonId")
    void changeTeacher(long lessonId, long newProfessorId);

    @Transactional
    @Modifying
    @Query("UPDATE Lesson l SET l.teacher.id = null WHERE l.id =:lessonId")
    void removeTeacherFromLesson(long lessonId);

    @Transactional
    @Modifying
    @Query("UPDATE Lesson l SET l.course.id =:newCourseId WHERE l.id =:lessonId")
    void changeCourse(long lessonId, long newCourseId);

    @Transactional
    @Modifying
    @Query("UPDATE Lesson l SET l.course.id = null WHERE l.id =:lessonId")
    void removeCourseFromLesson(long lessonId);

    @Transactional
    @Modifying
    @Query("UPDATE Lesson l SET l.group.id =:newGroupId WHERE l.id =:lessonId")
    void changeGroup(long lessonId, long newGroupId);

    @Transactional
    @Modifying
    @Query("UPDATE Lesson l SET l.group.id = null WHERE l.id =:lessonId")
    void removeGroupFromLesson(long lessonId);

}
