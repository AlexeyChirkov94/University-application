package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.RoleDao;
import ua.com.foxminded.university.entity.Role;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Log4j2
public class RoleDaoImpl extends AbstractPageableCrudDaoImpl<Role> implements RoleDao {

    private static final String FIND_BY_NAME_QUERY = "SELECT r from Role r WHERE name=:name";
    private static final String FIND_BY_USER_ID = "SELECT r.id, r.name from usersroles r " +
            "left join user_role ur on r.id = ur.role_id " +
            "left join users u on ur.user_id = u.id where u.id =:usedId";
    private static final String FIND_ALL_QUERY = "from Role order by id";
    private static final String DELETE_QUERY = "delete from Role where id=:deleteId";
    private static final String COUNT_QUERY = "select count(*) from Role";
    private static final String ADD_PRIVILEGE_TO_ROLE_QUERY = "INSERT INTO role_privilege (role_id, privilege_id) VALUES(:roleId, :addingPrivilegeId)";
    private static final String REMOVE_PRIVILEGE_FROM_ROLE_QUERY = "DELETE FROM role_privilege WHERE role_id =:roleId AND privilege_id =:removingPrivilegeId";

    public RoleDaoImpl(EntityManager entityManager) {
        super(entityManager, Role.class, FIND_ALL_QUERY, DELETE_QUERY, COUNT_QUERY);
    }

    @Override
    public List<Role> findByName(String name) {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_NAME_QUERY, Role.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public List<Role> findByUserId(long usedId) {
        Session session = entityManager.unwrap(Session.class);

        return session.createSQLQuery(FIND_BY_USER_ID)
                .addEntity(Role.class)
                .setParameter("usedId", usedId)
                .getResultList();
    }

    @Override
    public void addPrivilegeToRole(long roleId, long addingPrivilegeId) {
        Session session = entityManager.unwrap(Session.class);

        session.createSQLQuery(ADD_PRIVILEGE_TO_ROLE_QUERY)
                .setParameter("roleId", roleId)
                .setParameter("addingPrivilegeId", addingPrivilegeId)
                .executeUpdate();
    }

    @Override
    public void removePrivilegeFromRole(long roleId, long removingPrivilegeId) {
        Session session = entityManager.unwrap(Session.class);

        session.createSQLQuery(REMOVE_PRIVILEGE_FROM_ROLE_QUERY)
                .setParameter("roleId", roleId)
                .setParameter("removingPrivilegeId", removingPrivilegeId)
                .executeUpdate();
    }

    @Override
    protected Role insertCertainEntity(Role entity) {
        Session session = entityManager.unwrap(Session.class);

        Long idOfSavedEntity = (Long)session.save(entity);
        entity.setId(idOfSavedEntity);
        return entity;
    }

}
