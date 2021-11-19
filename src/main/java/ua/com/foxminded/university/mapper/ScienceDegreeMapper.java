package ua.com.foxminded.university.mapper;

import org.mapstruct.Mapper;
import ua.com.foxminded.university.dto.ScienceDegreeRequest;
import ua.com.foxminded.university.dto.ScienceDegreeResponse;
import ua.com.foxminded.university.entity.ScienceDegree;

@Mapper
public interface ScienceDegreeMapper {

    ScienceDegreeResponse mapEntityToDto (ScienceDegree scienceDegree);

    ScienceDegree mapDtoToEntity (ScienceDegreeRequest scienceDegreeRequest);

}
