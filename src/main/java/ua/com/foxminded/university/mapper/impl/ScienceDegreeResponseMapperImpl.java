package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.ScienceDegreeResponse;
import ua.com.foxminded.university.entity.ScienceDegree;
import ua.com.foxminded.university.mapper.interfaces.ScienceDegreeResponseMapper;

@Component
public class ScienceDegreeResponseMapperImpl implements ScienceDegreeResponseMapper {

    @Override
    public ScienceDegreeResponse mapEntityToDto(ScienceDegree entity) {
        if (entity == null) {
            return null;
        } else {
            return ScienceDegreeResponse.getById(entity.getId());
        }
    }

}
