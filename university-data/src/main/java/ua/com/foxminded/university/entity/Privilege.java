package ua.com.foxminded.university.entity;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(setterPrefix = "with")
public class Privilege {

    Long id;
    String name;
    List<Role> rolesOfPrivilege;

}
