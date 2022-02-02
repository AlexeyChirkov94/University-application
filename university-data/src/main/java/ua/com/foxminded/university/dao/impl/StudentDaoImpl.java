package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.entity.Student;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Log4j2
public class StudentDaoImpl extends AbstractPageableCrudDaoImpl<Student> implements StudentDao {

    private static final String FIND_GROUP_QUERY = "from Student s where s.group.id=:groupId ORDER BY s.id";
    private static final String FIND_ALL_QUERY = "from Student where type = 'student' order by id";
    private static final String DELETE_QUERY = "delete from Student where id=:deleteId";
    private static final String COUNT_QUERY = "select count(*) from Student where type = 'student'";
    private static final String FIND_BY_EMAIL_QUERY = "from Student where email=:email and type='student'";
    private static final String CHANGE_GROUP_QUERY = "UPDATE Student s SET s.group.id=:groupId WHERE id =:studentId";
    private static final String ADD_ROLE_TO_USER_QUERY = "INSERT INTO user_role (user_id, role_id) VALUES(:userId, :roleId)";
    private static final String REMOVE_ROLE_FROM_USER_QUERY = "DELETE FROM user_role WHERE user_id =:userId AND role_id =:roleId";

    public StudentDaoImpl(EntityManager entityManager) {
        super(entityManager, Student.class, FIND_ALL_QUERY, DELETE_QUERY, COUNT_QUERY);
    }

    @Override
    public List<Student> findByEmail(String email) {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_EMAIL_QUERY, Student.class)
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
    public List<Student> findByGroupId(long groupId){
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_GROUP_QUERY, Student.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }

    @Override
    public void leaveGroup(long studentId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_GROUP_QUERY)
                .setParameter("studentId", studentId)
                .setParameter("groupId", null)
                .executeUpdate();
    }

    @Override
    public void enterGroup(long studentId, long groupId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_GROUP_QUERY)
                .setParameter("studentId", studentId)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }

    @Override
    protected Student insertCertainEntity(Student student) {
        Session session = entityManager.unwrap(Session.class);

        Long idOfSavedEntity = (Long)session.save(student);
        student.setId(idOfSavedEntity);
        return student;
    }

}
