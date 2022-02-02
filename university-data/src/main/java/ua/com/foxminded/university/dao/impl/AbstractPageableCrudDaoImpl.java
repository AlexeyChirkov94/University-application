package ua.com.foxminded.university.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.CrudPageableDao;
import java.util.List;

@Transactional(transactionManager = "hibernateTransactionManager")
public abstract class AbstractPageableCrudDaoImpl<E> extends AbstractCrudDaoImpl<E> implements CrudPageableDao<E> {

    private final String countQuery;

    protected AbstractPageableCrudDaoImpl(SessionFactory sessionFactory, Class<E> typeParameterClass,
                                          String findAllQuery, String deleteQuery, String countQuery) {
        super(sessionFactory, typeParameterClass,  findAllQuery, deleteQuery);
        this.countQuery = countQuery;
    }

    public List<E> findAll(int page, int itemsPerPage){
        Session session = sessionFactory.getCurrentSession();

        int offset = itemsPerPage * (page - 1);

        return session.createQuery(findAllQuery, typeParameterClass)
                .setFirstResult(offset)
                .setMaxResults(itemsPerPage)
                .getResultList();
    }

    @Override
    public long count(){
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(countQuery, Long.class).uniqueResult();
    }

}
