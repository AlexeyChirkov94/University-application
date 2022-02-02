package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.PrivilegeDao;
import ua.com.foxminded.university.entity.Privilege;
import java.util.List;

@Repository
@Log4j
@Transactional(transactionManager = "hibernateTransactionManager")
public class PrivilegeDaoImpl extends AbstractPageableCrudDaoImpl<Privilege> implements PrivilegeDao {

    private static final String FIND_BY_NAME_QUERY = "from Privilege where name=:privilegeName";
    private static final String FIND_BY_ROLE_ID = "SELECT p.id, p.name from privileges p " +
            "left join role_privilege rp on p.id = rp.privilege_id " +
            "left join usersroles r on rp.role_id = r.id where r.id =:roleId";
    private static final String FIND_ALL_QUERY = "from Privilege order by id";
    private static final String DELETE_QUERY = "delete Privilege Course where id=:deleteId";
    private static final String COUNT_QUERY = "select count(*) from Privilege";

    public PrivilegeDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Privilege.class, FIND_ALL_QUERY, DELETE_QUERY, COUNT_QUERY);
    }

    @Override
    public List<Privilege> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(FIND_BY_NAME_QUERY, Privilege.class)
                .setParameter("privilegeName", name)
                .getResultList();
    }

    @Override
    public List<Privilege> findByRoleId(long roleId) {
        Session session = sessionFactory.getCurrentSession();

        return session.createSQLQuery(FIND_BY_ROLE_ID)
                .addEntity(Privilege.class)
                .setParameter("roleId", roleId)
                .getResultList();
    }

    @Override
    protected Privilege insertCertainEntity(Privilege entity) {
        Session session = sessionFactory.getCurrentSession();

        Long idOfSavedEntity = (Long)session.save(entity);
        entity.setId(idOfSavedEntity);
        return entity;
    }

}
