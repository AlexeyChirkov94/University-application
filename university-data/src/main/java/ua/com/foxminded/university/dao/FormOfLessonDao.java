package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.FormOfLesson;

import java.util.Optional;

public interface FormOfLessonDao extends CrudPageableDao<FormOfLesson> {

    Optional<FormOfLesson> findByName(String name);

}
