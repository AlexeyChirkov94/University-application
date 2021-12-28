package ua.com.foxminded.university.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.com.foxminded.university.entity.Role;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StudentResponse extends UserResponse {

    private GroupResponse groupResponse;
    private List<Role> roles;

}
