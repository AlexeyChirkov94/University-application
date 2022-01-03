package ua.com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CrudDao<E> {

    E save(E entity);

    void saveAll(List<E> entities);

    Optional<E> findById(Long id);

    List<E> findAll();

    void update(E entity);

    void updateAll(List<E> entities);

    boolean deleteById(Long id);

    boolean deleteByIds (Set<Long> ids);

}
