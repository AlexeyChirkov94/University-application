package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.entity.Group;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Log4j2
public class GroupDaoImpl extends AbstractPageableCrudDaoImpl<Group> implements GroupDao {

    private static final String FIND_BY_NAME_QUERY = "from Group where name=:groupName";
    private static final String FIND_ALL_QUERY = "from Group order by id";
    private static final String DELETE_QUERY = "delete from Group where id=:deleteId";
    private static final String COUNT_QUERY = "select count(*) from Group";
    private static final String CHANGE_FORM_OF_EDUCATION_QUERY = "UPDATE Group g SET g.formOfEducation.id=:newFormOfEducationId WHERE id =:groupId";
    private static final String CHANGE_DEPARTMENT_QUERY = "UPDATE Group g SET g.department.id=:newDepartmentId WHERE id =:groupId";
    private static final String FIND_BY_FROM_OF_EDUCATION_ID = "from Group where formOfEducation.id=:formOfEducationId";
    private static final String FIND_BY_DEPARTMENT_ID = "from Group where department.id=:departmentId";

    public GroupDaoImpl(EntityManager entityManager) {
        super(entityManager, Group.class, FIND_ALL_QUERY, DELETE_QUERY, COUNT_QUERY);
    }

    @Override
    public List<Group> findByName(String name) {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_NAME_QUERY, Group.class)
                .setParameter("groupName", name)
                .getResultList();
    }

    @Override
    public List<Group> findByFormOfEducation(long formOfEducationId){
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_FROM_OF_EDUCATION_ID, Group.class)
                .setParameter("formOfEducationId", formOfEducationId)
                .getResultList();
    }

    @Override
    public List<Group> findByDepartmentId(long departmentId){
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_DEPARTMENT_ID, Group.class)
                .setParameter("departmentId", departmentId)
                .getResultList();
    }

    @Override
    public void changeFormOfEducation(long groupId, long newFormOfEducationId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_FORM_OF_EDUCATION_QUERY)
                .setParameter("newFormOfEducationId", newFormOfEducationId)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }

    @Override
    public void removeFormOfEducationFromGroup(long groupId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_FORM_OF_EDUCATION_QUERY)
                .setParameter("newFormOfEducationId", null)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }

    @Override
    public void changeDepartment(long groupId, long newDepartmentId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_DEPARTMENT_QUERY)
                .setParameter("newDepartmentId", newDepartmentId)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }

    @Override
    public void removeDepartmentFromGroup(long groupId) {
        Session session = entityManager.unwrap(Session.class);

        session.createQuery(CHANGE_DEPARTMENT_QUERY)
                .setParameter("newDepartmentId", null)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }

    @Override
    protected Group insertCertainEntity(Group group) {
        Session session = entityManager.unwrap(Session.class);

        Long idOfSavedEntity = (Long)session.save(group);
        group.setId(idOfSavedEntity);
        return group;
    }

}
