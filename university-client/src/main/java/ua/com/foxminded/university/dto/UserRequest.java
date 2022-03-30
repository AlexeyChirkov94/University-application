package ua.com.foxminded.university.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserRequest{

    private Long id;

    @Email
    @NotBlank
    private String email;

    @Size(min = 6, max = 50)
    private String password;

    @Size(min = 2, max = 50)
    private String firstName;

    @Size(min = 2, max = 50)
    private String lastName;

}
