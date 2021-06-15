package ua.com.foxminded.university.mapper.interfaces;

public interface Mapper<E, D> {

    E mapDtoToEntity(D dto);

    D mapEntityToDto(E entity);

}
