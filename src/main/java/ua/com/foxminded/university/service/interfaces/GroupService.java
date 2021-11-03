package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    GroupResponse create(GroupRequest requestDto);

    Optional<GroupResponse> findById(long id);

    List<GroupResponse> findByFormOfEducation(long formOfEducationId);

    List<GroupResponse> findByDepartmentId(long departmentId);

    List<GroupResponse> findAll(String page);

    List<GroupResponse> findAll();

    void edit(GroupRequest requestDto);

    boolean deleteById(long id);

    void changeFormOfEducation(long groupId, long newFormOfEducationId);

    void removeFormOfEducationFromGroup(long groupId);

    void changeDepartment(long groupId, long departmentId);

    void removeDepartmentFromGroup(long groupId);

}
