package ua.com.foxminded.university.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.dto.ScienceDegreeResponse;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.entity.Professor;

@Mapper
public abstract class ProfessorMapper {

    @Autowired
    protected DepartmentMapper departmentMapper;

    @Autowired
    protected ScienceDegreeMapper scienceDegreeMapper;

    DepartmentResponse departmentResponse;
    ScienceDegreeResponse scienceDegreeResponse;

    {
        departmentResponse = new DepartmentResponse();
        departmentResponse.setId(0L);
        departmentResponse.setName("");
        scienceDegreeResponse = ScienceDegreeResponse.GRADUATE;
    }

    @Mapping(target = "id", expression = "java(professor.getId() == null ? 0 : professor.getId())")
    @Mapping(target = "firstName", expression = "java(professor.getFirstName() == null ? \"\" : professor.getFirstName())")
    @Mapping(target = "lastName", expression = "java(professor.getLastName() == null ? \"\" : professor.getLastName())")
    @Mapping(target = "email", expression = "java(professor.getEmail() == null ? \"\" : professor.getEmail())")
    @Mapping(target = "password", expression = "java(professor.getPassword() == null ? \"\" : professor.getPassword())")
    @Mapping(target = "departmentResponse", expression = "java(professor.getDepartment() == null ? " +
            "departmentResponse : departmentMapper.mapEntityToDto(professor.getDepartment()))")
    @Mapping(target = "scienceDegreeResponse", expression = "java(scienceDegreeMapper.mapEntityToDto(professor.getScienceDegree()))")
    public abstract ProfessorResponse mapEntityToDto (Professor professor);

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withFirstName", source = "firstName")
    @Mapping(target = "withLastName", source = "lastName")
    @Mapping(target = "withEmail", source = "email")
    @Mapping(target = "withPassword", source = "password")
    public abstract Professor mapDtoToEntity (ProfessorRequest professorRequest);

}
