package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.FormOfLesson;
import java.util.List;

public interface FormOfLessonDao extends CrudPageableDao<FormOfLesson> {

    List<FormOfLesson> findByName(String name);

}
