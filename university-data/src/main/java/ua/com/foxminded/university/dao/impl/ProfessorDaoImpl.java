package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.ProfessorDao;
import ua.com.foxminded.university.entity.Professor;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Log4j2
public class ProfessorDaoImpl extends AbstractPageableCrudDaoImpl<Professor> implements ProfessorDao {

    private static final String FIND_QUERY = "SELECT u.type, u.id, u.first_name, u.last_name, u.email, u.password, " +
            "u.department_id, d.name as department_name, u.sciencedegree_id " +
            "from users u left join departments d on u.department_id = d.id ";
    private static final String FIND_ALL_QUERY = "from Professor where type = 'professor' order by id";
    private static final String DELETE_QUERY = "delete from Professor where id=:deleteId";
    private static final String COUNT_QUERY = "select count(*) from Professor where type = 'professor'";
    private static final String FIND_BY_EMAIL_QUERY = "from Professor where email=:email and type='professor'";
    private static final String FIND_BY_COURSE_ID = FIND_QUERY + "left join professor_course pc on u.id = pc.professor_id " +
        "where pc.course_id =:courseId";
    private static final String FIND_BY_DEPARTMENT_ID = "from Professor where department.id=:departmentId";
    private static final String CHANGE_SCIENCE_DEGREE_QUERY = "UPDATE users SET sciencedegree_id =:idNewScienceDegree WHERE id =:professorId";
    private static final String CHANGE_DEPARTMENT_QUERY = "UPDATE Professor p SET p.department.id=:departmentId WHERE p.id =:professorId";
    private static final String ADD_ROLE_TO_USER_QUERY = "INSERT INTO user_role (user_id, role_id) VALUES(:userId, :roleId)";
    private static final String REMOVE_ROLE_FROM_USER_QUERY = "DELETE FROM user_role WHERE user_id =:userId AND role_id =:roleId";

    public ProfessorDaoImpl(EntityManager entityManager) {
        super(entityManager, Professor.class, FIND_ALL_QUERY, DELETE_QUERY, COUNT_QUERY);
    }

    @Override
    public List<Professor> findByEmail(String email) {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_EMAIL_QUERY, Professor.class)
                .setParameter("email", email)
                .getResultList();
    }

    @Override
    public void addRoleToUser(long userId, long addingRoleId) {
        Session session = entityManager.unwrap(Session.class);

        session.createSQLQuery(ADD_ROLE_TO_USER_QUERY)
                .setParameter("userId", userId)
                .setParameter("roleId", addingRoleId)
                .executeUpdate();
    }

    @Override
    public void removeRoleFromUser(long userId, long removingRoleId) {
        Session session = entityManager.unwrap(Session.class);

        session.createSQLQuery(REMOVE_ROLE_FROM_USER_QUERY)
                .setParameter("userId", userId)
                .setParameter("roleId", removingRoleId)
                .executeUpdate();
    }

    @Override
    public void changeScienceDegree(long professorId, int idNewScienceDegree) {
        Session session = entityManager.unwrap(Session.class);

        session.createSQLQuery(CHANGE_SCIENCE_DEGREE_QUERY)
                .setParameter("professorId", professorId)
                .setParameter("idNewScienceDegree", idNewScienceDegree)
                .executeUpdate();
    }

    @Override
    public List<Professor> findByCourseId(long courseId) {
        Session session = entityManager.unwrap(Session.class);

        return session.createSQLQuery(FIND_BY_COURSE_ID)
                .addEntity(Professor.class)
                .setParameter("courseId", courseId)
                .getResultList();
    }

    @Override
    public List<Professor> findByDepartmentId(long departmentId) {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_DEPARTMENT_ID, Professor.class)
                .setParameter("departmentId", departmentId)
                .getResultList();
    }

    @Override
    public void changeDepartment(long professorId, long departmentId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_DEPARTMENT_QUERY)
                .setParameter("professorId", professorId)
                .setParameter("departmentId", departmentId)
                .executeUpdate();
    }

    @Override
    public void removeDepartmentFromProfessor(long professorId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_DEPARTMENT_QUERY)
                .setParameter("professorId", professorId)
                .setParameter("departmentId", null)
                .executeUpdate();
    }

    @Override
    protected Professor insertCertainEntity(Professor professor) {
        Session session = entityManager.unwrap(Session.class);

        Long idOfSavedEntity = (Long)session.save(professor);
        professor.setId(idOfSavedEntity);
        return professor;
    }

}
