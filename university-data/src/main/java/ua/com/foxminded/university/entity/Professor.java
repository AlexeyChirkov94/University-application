package ua.com.foxminded.university.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Column;
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
public class Professor extends User {

    private static final String TYPE = "professor";

    @ManyToOne
    @JoinColumn(name="department_id")
    Department department;

    @Column(name="scienceDegree_id")
    ScienceDegree scienceDegree;

    public ScienceDegree getScienceDegree() {
        return (scienceDegree == null) ? ScienceDegree.GRADUATE : scienceDegree;
    }

    @Builder(setterPrefix = "with")
    protected Professor(Long id, String email, String password, String firstName, String lastName, Department department,
                     ScienceDegree scienceDegree) {
        super(id, email, password, firstName, lastName, TYPE);
        this.department = department;
        this.scienceDegree = scienceDegree;
    }

    public Professor() {
        this.type = TYPE;
    }

}
