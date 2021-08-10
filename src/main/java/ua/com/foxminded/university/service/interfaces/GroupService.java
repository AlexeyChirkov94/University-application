package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;
import java.util.List;
import java.util.Optional;

public interface GroupService {

    GroupResponse register(GroupRequest requestDto);

    Optional<GroupResponse> findById(long id);

    List<GroupResponse> findAll(String page);

    List<GroupResponse> findAll();

    void edit(GroupRequest requestDto);

    boolean deleteById(long id);

    void changeFormOfEducation(long groupId, long newFormOfEducationId);

    void changeDepartment(long groupId, long departmentId);

}
