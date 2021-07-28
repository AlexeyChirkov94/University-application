package ua.com.foxminded.university.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    protected Long id;
    protected String email;
    protected String password;
    protected String firstName;
    protected String lastName;

}
