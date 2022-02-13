package ua.com.foxminded.university.dao.impl;

import org.hibernate.Session;
import ua.com.foxminded.university.dao.CrudPageableDao;
import javax.persistence.EntityManager;
import java.util.List;

public abstract class AbstractPageableCrudDaoImpl<E> extends AbstractCrudDaoImpl<E> implements CrudPageableDao<E> {

    private final String countQuery;

    protected AbstractPageableCrudDaoImpl(EntityManager entityManager, Class<E> typeParameterClass,
                                          String findAllQuery, String deleteQuery, String countQuery) {
        super(entityManager, typeParameterClass,  findAllQuery, deleteQuery);
        this.countQuery = countQuery;
    }

    public List<E> findAll(int page, int itemsPerPage){
        Session session = entityManager.unwrap(Session.class);

        int offset = itemsPerPage * (page - 1);

        return session.createQuery(findAllQuery, typeParameterClass)
                .setFirstResult(offset)
                .setMaxResults(itemsPerPage)
                .getResultList();
    }

    @Override
    public long count(){
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(countQuery, Long.class).uniqueResult();
    }

}
