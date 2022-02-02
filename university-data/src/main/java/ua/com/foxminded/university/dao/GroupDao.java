package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.Group;
import java.util.List;

public interface GroupDao extends CrudPageableDao<Group> {

    List<Group> findByName(String name);

    List<Group> findByFormOfEducation(long formOfEducationId);

    List<Group> findByDepartmentId(long formOfEducationId);

    void changeFormOfEducation(long groupId, long newFormOfEducationId);

    void removeFormOfEducationFromGroup(long groupId);

    void changeDepartment(long groupId, long departmentId);

    void removeDepartmentFromGroup(long groupId);

}
