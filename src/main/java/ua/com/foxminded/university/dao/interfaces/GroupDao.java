package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.entity.Group;
import java.util.Optional;

public interface GroupDao extends CrudPageableDao<Group> {

    Optional<Group> findByName(String name);

    void changeFormOfEducation(long groupId, long newFormOfEducationId);

    void changeDepartment(long groupId, long departmentId);

}
