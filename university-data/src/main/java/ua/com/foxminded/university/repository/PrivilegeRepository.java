package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.entity.Privilege;
import java.util.List;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    List<Privilege> findAllByName(String name);

    @Transactional
    @Modifying
    @Query(value = "SELECT p.id, p.name from privileges p left join role_privilege rp on p.id = rp.privilege_id " +
            "left join roles_of_users r on rp.role_id = r.id where r.id =:roleId",
            nativeQuery = true)
    List<Privilege> findAllByRoleId(long roleId);

}