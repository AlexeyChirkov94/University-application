package ua.com.foxminded.university.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class StudentResponse extends UserResponse {

    private GroupResponse groupResponse;

}
