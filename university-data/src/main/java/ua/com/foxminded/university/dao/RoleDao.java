package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.Role;
import java.util.List;

public interface RoleDao extends CrudPageableDao<Role>{

    List<Role> findByName(String name);

    List<Role> findByUserId(long id);

    void addPrivilegeToRole(long roleId, long addingPrivilegeId);

    void removePrivilegeFromRole(long roleId, long removingPrivilegeId);

}
