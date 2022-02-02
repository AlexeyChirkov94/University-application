package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.Privilege;
import java.util.List;

public interface PrivilegeDao extends CrudPageableDao<Privilege> {

    List<Privilege> findByName(String name);

    List<Privilege> findByRoleId(long id);

}
