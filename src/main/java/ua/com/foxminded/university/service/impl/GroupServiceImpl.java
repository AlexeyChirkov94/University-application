package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dao.interfaces.FormOfEducationDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.interfaces.GroupRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.GroupResponseMapper;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.interfaces.GroupService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupServiceImpl extends AbstractPageableCrudService implements GroupService {

    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final FormOfEducationDao formOfEducationDao;
    private final DepartmentDao departmentDao;
    private final LessonDao lessonDao;
    private final GroupRequestMapper groupRequestMapper;
    private final GroupResponseMapper groupResponseMapper;

    @Override
    @Transactional(transactionManager = "txManager")
    public void changeFormOfEducation(long groupId, long newFormOfEducationId) {
        checkThatGroupExist(groupId);
        checkThatFormOfEducationExist(newFormOfEducationId);
        groupDao.changeFormOfEducation(groupId, newFormOfEducationId);
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public void removeFormOfEducationFromGroup(long groupId) {
        checkThatGroupExist(groupId);
        groupDao.removeFormOfEducationFromGroup(groupId);
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public void changeDepartment(long groupId, long newDepartmentId) {
        checkThatGroupExist(groupId);
        checkThatDepartmentExist(newDepartmentId);
        groupDao.changeDepartment(groupId, newDepartmentId);
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public void removeDepartmentFromGroup(long groupId) {
        checkThatGroupExist(groupId);
        groupDao.removeDepartmentFromGroup(groupId);
    }

    @Override
    public GroupResponse create(GroupRequest groupRequest) {
        if (groupDao.findByName(groupRequest.getName()).isPresent()){
            throw new EntityAlreadyExistException("Group with same name already exist");
        } else {
            Group groupBeforeSave = groupRequestMapper.mapDtoToEntity(groupRequest);
            Group groupAfterSave = groupDao.save(groupBeforeSave);

            return groupResponseMapper.mapEntityToDto(groupAfterSave);
        }
    }

    @Override
    public GroupResponse findById(long id) {
        Group group = groupDao.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no group with id: " + id));

        return groupResponseMapper.mapEntityToDto(group);
    }

    @Override
    public List<GroupResponse> findByFormOfEducation(long formOfEducationId) {
        checkThatFormOfEducationExist(formOfEducationId);

        return groupDao.findByFormOfEducation(formOfEducationId).stream().map(groupResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupResponse> findByDepartmentId(long departmentId) {
        checkThatDepartmentExist(departmentId);

        return groupDao.findByDepartmentId(departmentId).stream().map(groupResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupResponse> findAll(String page) {
        final long itemsCount = groupDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return groupDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(groupResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupResponse> findAll() {

        return groupDao.findAll().stream()
                .map(groupResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(GroupRequest groupRequest) {
        groupDao.update(groupRequestMapper.mapDtoToEntity(groupRequest));
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public boolean deleteById(long id) {
        if(groupDao.findById(id).isPresent()){

            List<Student> groupStudents = studentDao.findByGroupId(id);
            for(Student student : groupStudents){
                studentDao.leaveGroup(student.getId());
            }

            List<Lesson> groupLesson = lessonDao.findByGroupId(id);
            for(Lesson lesson : groupLesson){
                lessonDao.removeGroupFromLesson(lesson.getId());
            }

            return groupDao.deleteById(id);
        }
        return false;
    }

    private void checkThatGroupExist(long groupId){
        if (!groupDao.findById(groupId).isPresent()) {
            throw new EntityDontExistException("There no group with this id:" + groupId);
        }
    }

    private void checkThatFormOfEducationExist(long formOfEducationId){
        if (!formOfEducationDao.findById(formOfEducationId).isPresent()) {
            throw new EntityDontExistException("There no formOfEducation with this id:" + formOfEducationId);
        }
    }

    private void checkThatDepartmentExist(long departmentId){
        if (!departmentDao.findById(departmentId).isPresent()) {
            throw new EntityDontExistException("There no department with this id:" + departmentId);
        }
    }

}
