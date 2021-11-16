package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import java.util.List;

public interface StudentService extends UserService<StudentRequest, StudentResponse> {

    boolean leaveGroup(long studentId);

    boolean changeGroup(long studentId, long groupId);

    List<StudentResponse> findByGroupId(long groupId);

}
