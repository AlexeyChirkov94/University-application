package ua.com.foxminded.university.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString(callSuper=true)
@Getter()
@EqualsAndHashCode(callSuper=true)
public class Student extends User {

    Group group;

    @Builder(setterPrefix = "with")
    protected Student(Long id, String email, String password, String firstName, String lastName, Group group) {
        super(id, email, password, firstName, lastName);
        this.group = group;
    }

}
