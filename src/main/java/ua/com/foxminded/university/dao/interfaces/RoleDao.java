package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.entity.Role;
import java.util.List;
import java.util.Optional;

public interface RoleDao extends CrudPageableDao<Role>{

    Optional<Role> findByName(String name);

    List<Role> findByUserId(long id);

    void addPrivilegeToRole(long roleId, long addingPrivilegeId);

    void removePrivilegeFromRole(long roleId, long removingPrivilegeId);

}
