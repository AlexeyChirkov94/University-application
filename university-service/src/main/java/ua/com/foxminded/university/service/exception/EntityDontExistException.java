package ua.com.foxminded.university.service.exception;

public class EntityDontExistException extends RuntimeException {

    public EntityDontExistException(String message) {
        super(message);
    }

}
