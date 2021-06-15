package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;

public interface GroupService extends CrudService<GroupRequest, GroupResponse>{

    void changeFormOfEducation(long groupId, long newFormOfEducationId);

}
