package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.FormOfLessonDao;
import ua.com.foxminded.university.entity.FormOfLesson;
import java.util.List;

@Repository
@Log4j
@Transactional(transactionManager = "hibernateTransactionManager")
public class FormOfLessonDaoImpl extends AbstractPageableCrudDaoImpl<FormOfLesson> implements FormOfLessonDao {

    private static final String FIND_BY_NAME_QUERY = "from FormOfLesson where name=:formOfLessonName";
    private static final String FIND_ALL_QUERY = "from FormOfLesson order by id";
    private static final String DELETE_QUERY = "delete from FormOfLesson where id=:deleteId";
    private static final String COUNT_QUERY = "select count(*) from FormOfLesson";

    public FormOfLessonDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, FormOfLesson.class, FIND_ALL_QUERY, DELETE_QUERY, COUNT_QUERY);
    }

    @Override
    public List<FormOfLesson> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(FIND_BY_NAME_QUERY, FormOfLesson.class)
                .setParameter("formOfLessonName", name)
                .getResultList();
    }

    @Override
    protected FormOfLesson insertCertainEntity(FormOfLesson formOflesson) {
        Session session = sessionFactory.getCurrentSession();

        Long idOfSavedEntity = (Long)session.save(formOflesson);
        formOflesson.setId(idOfSavedEntity);
        return formOflesson;
    }

}
