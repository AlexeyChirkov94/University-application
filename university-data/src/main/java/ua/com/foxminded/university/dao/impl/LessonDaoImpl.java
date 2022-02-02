package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.entity.Lesson;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Log4j2
public class LessonDaoImpl extends AbstractPageableCrudDaoImpl<Lesson> implements LessonDao {

    private static final String FIND_ALL_QUERY = "from Lesson order by id";
    private static final String DELETE_QUERY = "delete from Lesson where id=:deleteId";
    private static final String COUNT_QUERY = "select count(*) from Lesson";
    private static final String CHANGE_FORM_OF_LESSON_QUERY = "UPDATE Lesson l SET l.formOfLesson.id=:newFormOfLessonId WHERE l.id =:lessonId";
    private static final String CHANGE_TEACHER_QUERY = "UPDATE Lesson l SET l.teacher.id=:newProfessorId WHERE l.id =:lessonId";
    private static final String CHANGE_COURSE_QUERY = "UPDATE Lesson l SET l.course.id=:newCourseId WHERE l.id =:lessonId";
    private static final String CHANGE_GROUP_QUERY = "UPDATE Lesson l SET l.group.id=:newGroupId WHERE l.id =:lessonId";
    private static final String FIND_BY_GROUP_ID = "from Lesson where group.id=:groupId ORDER BY timeOfStartLesson";
    private static final String FIND_BY_PROFESSOR_ID = "from Lesson where teacher.id=:teacherId ORDER BY timeOfStartLesson";
    private static final String FIND_BY_COURSE_ID = "from Lesson where course.id=:courseId ORDER BY timeOfStartLesson";
    private static final String FIND_BY_FORM_OF_LESSON_ID = "from Lesson where formOfLesson.id=:formOfLessonId ORDER BY timeOfStartLesson";

    public LessonDaoImpl(EntityManager entityManager) {
        super(entityManager, Lesson.class, FIND_ALL_QUERY, DELETE_QUERY, COUNT_QUERY);
    }

    @Override
    public List<Lesson> findByGroupId(long groupId) {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_GROUP_ID, Lesson.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }

    @Override
    public List<Lesson> findByProfessorId(long professorId) {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_PROFESSOR_ID, Lesson.class)
                .setParameter("teacherId", professorId)
                .getResultList();
    }

    @Override
    public List<Lesson> findByCourseId(long courseId){
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_COURSE_ID, Lesson.class)
                .setParameter("courseId", courseId)
                .getResultList();
    }

    @Override
    public List<Lesson> findByFormOfLessonId(long formOfLessonId){
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_FORM_OF_LESSON_ID, Lesson.class)
                .setParameter("formOfLessonId", formOfLessonId)
                .getResultList();
    }

    @Override
    public void changeFormOfLesson(long lessonId, long newFormOfLessonId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_FORM_OF_LESSON_QUERY)
                .setParameter("newFormOfLessonId", newFormOfLessonId)
                .setParameter("lessonId", lessonId)
                .executeUpdate();
    }

    @Override
    public void changeTeacher(long lessonId, long newProfessorId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_TEACHER_QUERY)
                .setParameter("newProfessorId", newProfessorId)
                .setParameter("lessonId", lessonId)
                .executeUpdate();
    }

    @Override
    public void changeCourse(long lessonId, long newCourseId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_COURSE_QUERY)
                .setParameter("newCourseId", newCourseId)
                .setParameter("lessonId", lessonId)
                .executeUpdate();
    }

    @Override
    public void changeGroup(long lessonId, long newGroupId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_GROUP_QUERY)
                .setParameter("newGroupId", newGroupId)
                .setParameter("lessonId", lessonId)
                .executeUpdate();
    }

    @Override
    public void removeFormOfLessonFromLesson(long lessonId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_FORM_OF_LESSON_QUERY)
                .setParameter("newFormOfLessonId", null)
                .setParameter("lessonId", lessonId)
                .executeUpdate();
    }

    @Override
    public void removeTeacherFromLesson(long lessonId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_TEACHER_QUERY)
                .setParameter("newProfessorId", null)
                .setParameter("lessonId", lessonId)
                .executeUpdate();
    }

    @Override
    public void removeCourseFromLesson(long lessonId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_COURSE_QUERY)
                .setParameter("newCourseId", null)
                .setParameter("lessonId", lessonId)
                .executeUpdate();
    }

    @Override
    public void removeGroupFromLesson(long lessonId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_GROUP_QUERY)
                .setParameter("newGroupId", null)
                .setParameter("lessonId", lessonId)
                .executeUpdate();
    }

    @Override
    protected Lesson insertCertainEntity(Lesson lesson) {
        Session session = entityManager.unwrap(Session.class);

        Long idOfSavedEntity = (Long)session.save(lesson);
        lesson.setId(idOfSavedEntity);
        return lesson;
    }

}
