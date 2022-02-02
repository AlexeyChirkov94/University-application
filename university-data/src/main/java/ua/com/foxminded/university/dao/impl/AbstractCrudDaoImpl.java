package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.CrudDao;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j
@Transactional(transactionManager = "hibernateTransactionManager")
public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E> {

    protected final SessionFactory sessionFactory;
    protected final String deleteQuery;
    protected final String findAllQuery;
    protected final Class<E> typeParameterClass;

    protected AbstractCrudDaoImpl(SessionFactory sessionFactory, Class<E> typeParameterClass,
                                  String findAllQuery, String deleteQuery) {
        this.sessionFactory = sessionFactory;
        this.findAllQuery = findAllQuery;
        this.deleteQuery = deleteQuery;
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    public E save(E entity) {
        return insertCertainEntity(entity);
    }

    @Override
    public void saveAll(List<E> entities){
        entities.stream().forEach((e)->save(e));
    }

    @Override
    public Optional<E> findById(Long id){
        Session session = sessionFactory.getCurrentSession();

        return Optional.ofNullable(session.get(typeParameterClass, id));
    }

    @Override
    public List<E> findAll(){
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(findAllQuery, typeParameterClass).getResultList();
    }

    @Override
    public void update(E entity) {
        Session session = sessionFactory.getCurrentSession();

        session.update(entity);
    }

    @Override
    public void updateAll(List<E> entities) {
        entities.stream().forEach((e)->update(e));
    }

    @Override
    public void deleteById(Long id) {
        Session session = sessionFactory.getCurrentSession();

        session.createQuery(deleteQuery)
        .setParameter("deleteId", id)
        .executeUpdate();
    }

    @Override
    public void deleteByIds(Set<Long> ids) {
        ids.stream().forEach((e)->deleteById(e));
    }

    protected abstract E insertCertainEntity(E entity);

}
