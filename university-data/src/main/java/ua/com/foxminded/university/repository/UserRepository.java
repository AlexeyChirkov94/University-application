package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.entity.User;

import java.util.List;

public interface UserRepository<U extends User> extends JpaRepository<U, Long> {

    @Query("from Professor where email=:email and type='professor'")
    List<U> findAllByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_role (user_id, role_id) VALUES(:userId, :addingRoleId)",
            nativeQuery = true)
    void addRoleToUser(long userId, long addingRoleId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_role WHERE user_id =:userId AND role_id =:removingRoleId",
            nativeQuery = true)
    void removeRoleFromUser(long userId, long removingRoleId);

}
