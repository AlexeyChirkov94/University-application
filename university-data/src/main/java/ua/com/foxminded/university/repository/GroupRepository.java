package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.entity.Group;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAllByName(String name);

    List<Group> findAllByFormOfEducationIdOrderById(long formOfEducationId);

    List<Group> findAllByDepartmentIdOrderById(long formOfEducationId);

    @Transactional
    @Modifying
    @Query("UPDATE Group g SET g.formOfEducation.id=:newFormOfEducationId WHERE id =:groupId")
    void changeFormOfEducation(long groupId, long newFormOfEducationId);

    @Transactional
    @Modifying
    @Query("UPDATE Group g SET g.formOfEducation.id= null WHERE id =:groupId")
    void removeFormOfEducationFromGroup(long groupId);

    @Transactional
    @Modifying
    @Query("UPDATE Group g SET g.department.id=:newDepartmentId WHERE id =:groupId")
    void changeDepartment(long groupId, long newDepartmentId);

    @Transactional
    @Modifying
    @Query("UPDATE Group g SET g.department.id= null WHERE id =:groupId")
    void removeDepartmentFromGroup(long groupId);

}
