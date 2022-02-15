package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.university.entity.FormOfEducation;
import java.util.List;

public interface FormOfEducationRepository extends JpaRepository<FormOfEducation, Long> {

    List<FormOfEducation> findAllByName(String name);

}
