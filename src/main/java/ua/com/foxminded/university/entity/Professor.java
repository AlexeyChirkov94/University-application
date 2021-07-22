package ua.com.foxminded.university.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import java.util.List;

@ToString(callSuper=true)
@Getter()
@EqualsAndHashCode(callSuper=true)
public class Professor extends User {

    Department department;
    ScienceDegree scienceDegree;
    List<Course> courses;

    @Builder(setterPrefix = "with")
    protected Professor(Long id, String email, String password, String firstName, String lastName, Department department,
                     ScienceDegree scienceDegree, List<Course> courses) {
        super(id, email, password, firstName, lastName);
        this.department = department;
        this.scienceDegree = scienceDegree;
        this.courses = courses;
    }

}
