package ua.com.foxminded.university.service.validator;

public interface Validator<E> {
    void validate(E entity);
}
