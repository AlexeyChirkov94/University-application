package ua.com.foxminded.university.service.interfaces;

import java.util.List;
import java.util.Optional;

public interface CrudService<REQUEST, RESPONSE> {

    RESPONSE register(REQUEST requestDto);

    Optional<RESPONSE> findById(long id);

    List<RESPONSE> findAll(String page);

    void edit(REQUEST requestDto);

    boolean deleteById(long id);

}
