package ua.com.foxminded.university.service;

import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;
import java.util.List;

public interface GroupService {

    GroupResponse create(GroupRequest requestDto);

    GroupResponse findById(long id);

    List<GroupResponse> findByFormOfEducation(long formOfEducationId);

    List<GroupResponse> findByDepartmentId(long departmentId);

    List<GroupResponse> findAll(String page);

    List<GroupResponse> findAll();

    void edit(GroupRequest requestDto);

    void deleteById(long id);

    void changeFormOfEducation(long groupId, long newFormOfEducationId);

    void removeFormOfEducationFromGroup(long groupId);

    void changeDepartment(long groupId, long departmentId);

    void removeDepartmentFromGroup(long groupId);

}
