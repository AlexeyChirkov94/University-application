package ua.com.foxminded.university.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.entity.Professor;

import java.util.List;

public interface ProfessorRepository extends UserRepository<Professor> {

    @Query("from Professor where type = 'professor' order by id")
    List<Professor> findAll();

    @Query(
            value = "from Professor where type = 'professor' order by id",
            countQuery = "select count(*) from Student where type = 'professor'")
    Page<Professor> findAll(Pageable pageable);

    @Override
    @Query("from Professor where email=:email and type='professor'")
    List<Professor> findAllByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET sciencedegree_id =:idNewScienceDegree WHERE id =:professorId",
            nativeQuery = true)
    void changeScienceDegree(long professorId, int idNewScienceDegree);

    @Transactional
    @Modifying
    @Query(value = "SELECT u.type, u.id, u.first_name, u.last_name, u.email, u.password, u.department_id, " +
            "d.name as department_name, u.sciencedegree_id from users u " +
            "left join departments d on u.department_id = d.id " +
            "left join professor_course pc on u.id = pc.professor_id " +
            "where pc.course_id =:courseId order by id",
            nativeQuery = true)
    List<Professor> findAllByCourseId(long courseId);

    @Query("from Professor where department.id=:departmentId order by id")
    List<Professor> findAllByDepartmentId(long departmentId);

    @Transactional
    @Modifying
    @Query("UPDATE Professor p SET p.department.id=:departmentId WHERE p.id =:professorId")
    void changeDepartment(long professorId, long departmentId);

    @Transactional
    @Modifying
    @Query("UPDATE Professor p SET p.department.id = null WHERE p.id =:professorId")
    void removeDepartmentFromProfessor(long professorId);

}
