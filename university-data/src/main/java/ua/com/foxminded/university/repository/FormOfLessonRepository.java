package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.university.entity.FormOfLesson;
import java.util.List;

public interface FormOfLessonRepository extends JpaRepository<FormOfLesson, Long> {

    List<FormOfLesson> findAllByName(String name);

}
