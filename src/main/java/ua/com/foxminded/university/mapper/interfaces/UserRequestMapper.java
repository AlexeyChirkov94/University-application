package ua.com.foxminded.university.mapper.interfaces;

import ua.com.foxminded.university.dto.UserRequest;
import ua.com.foxminded.university.entity.User;

public interface UserRequestMapper <E extends User, D extends UserRequest> /*extends Mapper <E, D>*/{

    E mapDtoToEntity(D userDto);

    D mapEntityToDto(E entity);
}
