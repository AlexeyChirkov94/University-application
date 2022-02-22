package ua.com.foxminded.university.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.entity.Student;

import java.util.List;

public interface StudentRepository extends UserRepository<Student> {

    @Query("from Student where type = 'student' order by id")
    List<Student> findAll();

    @Query(
            value = "from Student where type = 'student' order by id",
            countQuery = "select count(*) from Student where type = 'student'")
    Page<Student> findAll(Pageable pageable);

    @Override
    @Query("from Student where email=:email and type='student'")
    List<Student> findAllByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Student s SET s.group.id = null WHERE id =:studentId")
    void leaveGroup(long studentId);

    @Transactional
    @Modifying
    @Query("UPDATE Student s SET s.group.id=:groupId WHERE id =:studentId")
    void enterGroup(long studentId, long groupId);

    @Query("from Student s where s.group.id=:groupId ORDER BY s.id")
    List<Student> findAllByGroupId(long groupId);

}
