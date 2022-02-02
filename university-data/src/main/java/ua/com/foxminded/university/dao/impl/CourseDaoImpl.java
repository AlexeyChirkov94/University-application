package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.entity.Course;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Log4j2
public class CourseDaoImpl extends AbstractPageableCrudDaoImpl<Course> implements CourseDao {

    private static final String FIND_QUERY = "SELECT c.id, c.name, c.department_id, d.name as department_name " +
            "from courses c left join departments d on c.department_id = d.id ";
    private static final String FIND_BY_NAME_QUERY = "from Course where name=:courseName";
    private static final String FIND_ALL_QUERY = "from Course order by id";
    private static final String DELETE_QUERY = "delete from Course where id=:deleteId";
    private static final String COUNT_QUERY = "select count(*) from Course";
    private static final String FIND_BY_PROFESSOR_ID = FIND_QUERY + "left join professor_course pc on c.id = pc.course_id " +
            "where pc.professor_id =:professorId order by c.id";
    private static final String FIND_BY_DEPARTMENT_ID = "from Course where department.id=:departmentId";
    private static final String REMOVE_COURSE_FROM_PROFESSOR_COURSE_LIST_QUERY =
            "DELETE FROM professor_course WHERE professor_id =:professorId AND course_id =:courseId";
    private static final String ADD_COURSE_TO_PROFESSOR_COURSE_LIST_QUERY =
            "INSERT INTO professor_course (professor_id, course_id) VALUES(:professorId, :courseId)";
    private static final String CHANGE_DEPARTMENT_QUERY = "UPDATE Course c SET c.department.id=:newDepartmentId WHERE id =:courseId";

    public CourseDaoImpl(EntityManager entityManager) {
        super(entityManager, Course.class, FIND_ALL_QUERY, DELETE_QUERY, COUNT_QUERY);
    }

    @Override
    public List<Course> findByName(String name) {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_NAME_QUERY, Course.class)
                .setParameter("courseName", name)
                .getResultList();
    }

    @Override
    public void addCourseToProfessorCourseList(long courseId, long professorId) {
        Session session = entityManager.unwrap(Session.class);

        session.createSQLQuery(ADD_COURSE_TO_PROFESSOR_COURSE_LIST_QUERY)
                .setParameter("courseId", courseId)
                .setParameter("professorId", professorId)
                .executeUpdate();
    }

    @Override
    public void removeCourseFromProfessorCourseList(long courseId, long professorId) {
        Session session = entityManager.unwrap(Session.class);

        session.createSQLQuery(REMOVE_COURSE_FROM_PROFESSOR_COURSE_LIST_QUERY)
                .setParameter("courseId", courseId)
                .setParameter("professorId", professorId)
                .executeUpdate();
    }

    @Override
    public void changeDepartment(long courseId, long departmentId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_DEPARTMENT_QUERY)
                .setParameter("newDepartmentId", departmentId)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }

    @Override
    public void removeDepartmentFromCourse(long courseId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_DEPARTMENT_QUERY)
                .setParameter("newDepartmentId", null)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }

    @Override
    public List<Course> findByProfessorId(long professorId){
        Session session = entityManager.unwrap(Session.class);

        return session.createSQLQuery(FIND_BY_PROFESSOR_ID)
                .addEntity(Course.class)
                .setParameter("professorId", professorId)
                .getResultList();
    }

    @Override
    public List<Course> findByDepartmentId(long departmentId) {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_DEPARTMENT_ID, Course.class)
                .setParameter("departmentId", departmentId)
                .getResultList();
    }

    @Override
    protected Course insertCertainEntity(Course course) {
        Session session = entityManager.unwrap(Session.class);

        Long idOfSavedEntity = (Long)session.save(course);
        course.setId(idOfSavedEntity);
        return course;
    }

}
