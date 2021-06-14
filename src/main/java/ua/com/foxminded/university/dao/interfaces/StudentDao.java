package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.domain.Student;

public interface StudentDao extends CrudPageableDao<Student> {

    void leaveGroup(long studentId);

    void enterGroup(long studentId, long groupId);

}
