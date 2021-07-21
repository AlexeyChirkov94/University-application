package ua.com.foxminded.university.mapper.interfaces;

import ua.com.foxminded.university.dto.UserRequest;
import ua.com.foxminded.university.dto.UserResponse;
import ua.com.foxminded.university.entity.User;

public interface UserResponseMapper <E extends User, D extends UserResponse> /*extends Mapper <E, D>*/{
    E
    mapDtoToEntity(D userDto);

    D mapEntityToDto(E entity);

}
