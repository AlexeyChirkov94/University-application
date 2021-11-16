package ua.com.foxminded.university.mapper.interfaces;

public interface ResponseMapper<E, D> {

    D mapEntityToDto(E entity);

}
