package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.FormOfEducationDao;
import ua.com.foxminded.university.entity.FormOfEducation;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Log4j2
public class FormOfEducationDaoImpl extends AbstractPageableCrudDaoImpl<FormOfEducation> implements FormOfEducationDao {

    private static final String FIND_BY_NAME_QUERY = "from FormOfEducation where name=:formOfEducationName";
    private static final String FIND_ALL_QUERY = "from FormOfEducation order by id";
    private static final String DELETE_QUERY = "delete from FormOfEducation where id=:deleteId";
    private static final String COUNT_QUERY = "select count(*) from FormOfEducation";

    public FormOfEducationDaoImpl(EntityManager entityManager) {
        super(entityManager, FormOfEducation.class, FIND_ALL_QUERY, DELETE_QUERY, COUNT_QUERY);
    }

    @Override
    public List<FormOfEducation> findByName(String name) {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(FIND_BY_NAME_QUERY, FormOfEducation.class)
                .setParameter("formOfEducationName", name)
                .getResultList();
    }

    @Override
    protected FormOfEducation insertCertainEntity(FormOfEducation formOfEducation) {
        Session session = entityManager.unwrap(Session.class);

        Long idOfSavedEntity = (Long)session.save(formOfEducation);
        formOfEducation.setId(idOfSavedEntity);
        return formOfEducation;
    }

}
