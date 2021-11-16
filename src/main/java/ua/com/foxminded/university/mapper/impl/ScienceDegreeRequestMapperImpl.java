package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.ScienceDegreeRequest;
import ua.com.foxminded.university.entity.ScienceDegree;
import ua.com.foxminded.university.mapper.interfaces.ScienceDegreeRequestMapper;

@Component
public class ScienceDegreeRequestMapperImpl implements ScienceDegreeRequestMapper {

    @Override
    public ScienceDegree mapDtoToEntity(ScienceDegreeRequest dto) {
        if (dto == null) {
            return null;
        } else {
            return ScienceDegree.getById(dto.getId());
        }
    }

}
