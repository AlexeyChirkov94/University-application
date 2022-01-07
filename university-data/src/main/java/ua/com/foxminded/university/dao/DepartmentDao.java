package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.Department;

import java.util.Optional;

public interface DepartmentDao extends CrudPageableDao<Department> {

    Optional<Department> findByName(String name);

}
