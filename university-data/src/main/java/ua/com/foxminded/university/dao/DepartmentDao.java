package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.Department;
import java.util.List;

public interface DepartmentDao extends CrudPageableDao<Department> {

    List<Department> findByName(String name);

}
