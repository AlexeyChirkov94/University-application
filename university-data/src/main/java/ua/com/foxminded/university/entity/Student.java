package ua.com.foxminded.university.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="users")
@ToString(callSuper = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Student extends User {

    private static final String TYPE = "student";

    @ManyToOne
    @JoinColumn(name="group_id")
    Group group;

    @Builder(setterPrefix = "with")
    protected Student(Long id, String email, String password, String firstName, String lastName, Group group) {
        super(id, email, password, firstName, lastName, TYPE);
        this.group = group;
    }

    public Student() {
        this.type = TYPE;
    }

}
