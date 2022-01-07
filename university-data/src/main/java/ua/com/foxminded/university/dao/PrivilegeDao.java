package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.Privilege;

import java.util.List;
import java.util.Optional;

public interface PrivilegeDao extends CrudPageableDao<Privilege> {

    Optional<Privilege> findByName(String name);

    List<Privilege> findByRoleId(long id);

}
