package ua.com.foxminded.university.mapper.interfaces;


public interface UserMapper<E, D> extends Mapper <E, D>{

    E mapDtoToEntity(D userDto);

    D mapEntityToDto(E entity);

}
