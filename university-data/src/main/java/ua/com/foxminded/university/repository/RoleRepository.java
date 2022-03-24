package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.entity.Role;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findAllByName(String name);

    @Query(value = "SELECT r.id, r.name from roles_of_users r left join user_role ur on r.id = ur.role_id " +
            "left join users u on ur.user_id = u.id where u.id =:usedId", nativeQuery = true)
    List<Role> findAllByUserId(long usedId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO role_privilege (role_id, privilege_id) VALUES(:roleId, :addingPrivilegeId)",
            nativeQuery = true)
    void addPrivilegeToRole(long roleId, long addingPrivilegeId);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM role_privilege WHERE role_id =:roleId AND privilege_id =:removingPrivilegeId",
            nativeQuery = true)
    void removePrivilegeFromRole(long roleId, long removingPrivilegeId);

}
