package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.FormOfEducation;
import java.util.List;

public interface FormOfEducationDao extends CrudPageableDao<FormOfEducation>{

    List<FormOfEducation> findByName(String name);

}
