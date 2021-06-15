package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;

public interface StudentService extends UserService<StudentRequest, StudentResponse> {

    boolean leaveGroup(long studentId);

    boolean enterGroup(long studentId, long groupId);

}
