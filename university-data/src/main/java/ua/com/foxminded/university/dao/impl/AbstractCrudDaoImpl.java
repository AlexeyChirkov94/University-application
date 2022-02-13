package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.CrudDao;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Transactional
public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E> {

    @Autowired
    protected final EntityManager entityManager;
    protected final String deleteQuery;
    protected final String findAllQuery;
    protected final Class<E> typeParameterClass;

    protected AbstractCrudDaoImpl(EntityManager entityManager, Class<E> typeParameterClass,
                                  String findAllQuery, String deleteQuery) {
        this.entityManager = entityManager;
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
        Session session = entityManager.unwrap(Session.class);

        return Optional.ofNullable(session.get(typeParameterClass, id));
    }

    @Override
    public List<E> findAll(){
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(findAllQuery, typeParameterClass).getResultList();
    }

    @Override
    public void update(E entity) {
        Session session = entityManager.unwrap(Session.class);

        session.update(entity);
    }

    @Override
    public void updateAll(List<E> entities) {
        entities.stream().forEach((e)->update(e));
    }

    @Override
    public void deleteById(Long id) {
        Session session = entityManager.unwrap(Session.class);

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
