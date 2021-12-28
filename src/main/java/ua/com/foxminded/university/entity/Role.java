package ua.com.foxminded.university.entity;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder(setterPrefix = "with")
public class Role {

    Long id;
    String name;
    List<Privilege> privilegesOfRole;

}
