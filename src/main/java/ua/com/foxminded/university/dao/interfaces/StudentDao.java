package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.entity.Student;

import java.util.List;

public interface StudentDao extends UserDao<Student> {

    void leaveGroup(long studentId);

    void enterGroup(long studentId, long groupId);

    List<Student> findByGroupId(long groupId);

}
