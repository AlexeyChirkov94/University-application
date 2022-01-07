package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.FormOfEducation;

import java.util.Optional;

public interface FormOfEducationDao extends CrudPageableDao<FormOfEducation>{

    Optional<FormOfEducation> findByName(String name);

}