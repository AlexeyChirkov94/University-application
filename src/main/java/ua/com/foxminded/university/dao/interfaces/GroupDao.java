package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.domain.Group;

public interface GroupDao extends CrudPageableDao<Group> {

    void changeFormOfEducation(long groupId, long newFormOfEducationId);

}
