package ua.com.foxminded.university.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Table(name="users")
@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@MappedSuperclass
public class User {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name="email")
    protected String email;

    @Column(name="password")
    protected String password;

    @Column(name="first_name")
    protected String firstName;

    @Column(name="last_name")
    protected String lastName;

    @Column(name="type")
    protected String type;

}
