package ua.com.foxminded.university.mapper.interfaces;

public interface RequestMapper<E, D> {

    E mapDtoToEntity(D dto);

}