package ua.com.foxminded.university.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.Student;

@Mapper
public abstract class StudentMapper {

    @Autowired
    protected GroupMapper groupMapper;

    @Mapping(target = "id", expression = "java(student.getId() == null ? 0 : student.getId())")
    @Mapping(target = "firstName", expression = "java(student.getFirstName() == null ? \"\" : student.getFirstName())")
    @Mapping(target = "lastName", expression = "java(student.getLastName() == null ? \"\" : student.getLastName())")
    @Mapping(target = "email", expression = "java(student.getEmail() == null ? \"\" : student.getEmail())")
    @Mapping(target = "password", expression = "java(student.getPassword() == null ? \"\" : student.getPassword())")
    @Mapping(target = "groupResponse", expression = "java(groupMapper.mapEntityToDto(student.getGroup()))")
    public abstract StudentResponse mapEntityToDto (Student student);

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withFirstName", source = "firstName")
    @Mapping(target = "withLastName", source = "lastName")
    @Mapping(target = "withEmail", source = "email")
    @Mapping(target = "withPassword", source = "password")
    public abstract Student mapDtoToEntity (StudentRequest studentRequest);

}
