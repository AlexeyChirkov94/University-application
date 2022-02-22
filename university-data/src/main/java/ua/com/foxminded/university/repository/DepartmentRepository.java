package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.university.entity.Department;
import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findAllByName(String name);

}
