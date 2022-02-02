package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.DepartmentDao;
import ua.com.foxminded.university.entity.Department;
import java.util.List;

@Repository
@Log4j
@Transactional(transactionManager = "hibernateTransactionManager")
public class DepartmentDaoImpl extends AbstractPageableCrudDaoImpl<Department> implements DepartmentDao {

    private static final String FIND_BY_NAME_QUERY = "from Department where name=:departmentName";
    private static final String FIND_ALL_QUERY = "from Department order by id";
    private static final String DELETE_QUERY = "delete from Department where id=:deleteId";
    private static final String COUNT_QUERY = "select count(*) from Department";

    public DepartmentDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Department.class, FIND_ALL_QUERY, DELETE_QUERY, COUNT_QUERY);
    }

    @Override
    public List<Department> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(FIND_BY_NAME_QUERY, Department.class)
                .setParameter("departmentName", name)
                .getResultList();
    }
    
    @Override
    protected Department insertCertainEntity(Department department){
        Session session = sessionFactory.getCurrentSession();

        Long idOfSavedEntity = (Long)session.save(department);
        department.setId(idOfSavedEntity);
        return department;
    }

}
