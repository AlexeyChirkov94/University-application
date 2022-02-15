package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.repository.DepartmentRepository;
import ua.com.foxminded.university.repository.FormOfEducationRepository;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.GroupMapper;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.GroupService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class GroupServiceImpl extends AbstractPageableCrudService implements GroupService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final FormOfEducationRepository formOfEducationRepository;
    private final DepartmentRepository departmentRepository;
    private final LessonRepository lessonRepository;
    private final GroupMapper groupMapper;

    @Override
    public void changeFormOfEducation(long groupId, long newFormOfEducationId) {
        checkThatGroupExist(groupId);
        checkThatFormOfEducationExist(newFormOfEducationId);
        groupRepository.changeFormOfEducation(groupId, newFormOfEducationId);
    }

    @Override
    public void removeFormOfEducationFromGroup(long groupId) {
        checkThatGroupExist(groupId);
        groupRepository.removeFormOfEducationFromGroup(groupId);
    }

    @Override
    public void changeDepartment(long groupId, long newDepartmentId) {
        checkThatGroupExist(groupId);
        checkThatDepartmentExist(newDepartmentId);
        groupRepository.changeDepartment(groupId, newDepartmentId);
    }

    @Override
    public void removeDepartmentFromGroup(long groupId) {
        checkThatGroupExist(groupId);
        groupRepository.removeDepartmentFromGroup(groupId);
    }

    @Override
    public GroupResponse create(GroupRequest groupRequest) {
        if (!groupRepository.findAllByName(groupRequest.getName()).isEmpty()){
            throw new EntityAlreadyExistException("Group with same name already exist");
        } else {
            Group groupBeforeSave = groupMapper.mapDtoToEntity(groupRequest);
            Group groupAfterSave = groupRepository.save(groupBeforeSave);

            if(groupRequest.getDepartmentId() != 0L){
                changeDepartment(groupAfterSave.getId(), groupRequest.getDepartmentId());
            }

            if(groupRequest.getFormOfEducationId() != 0L){
                changeFormOfEducation(groupAfterSave.getId(), groupRequest.getFormOfEducationId());
            }

            return groupMapper.mapEntityToDto(groupAfterSave);
        }
    }

    @Override
    public GroupResponse findById(long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no group with id: " + id));

        return groupMapper.mapEntityToDto(group);
    }

    @Override
    public List<GroupResponse> findByFormOfEducation(long formOfEducationId) {
        checkThatFormOfEducationExist(formOfEducationId);

        return groupRepository.findAllByFormOfEducationIdOrderById(formOfEducationId).stream().map(groupMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupResponse> findByDepartmentId(long departmentId) {
        checkThatDepartmentExist(departmentId);

        return groupRepository.findAllByDepartmentIdOrderById(departmentId).stream().map(groupMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupResponse> findAll(String page) {
        final long itemsCount = groupRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return groupRepository.findAll(PageRequest.of(pageNumber - 1, ITEMS_PER_PAGE, Sort.by("id")))
                .stream().map(groupMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupResponse> findAll() {

        return groupRepository.findAll(Sort.by("id")).stream()
                .map(groupMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(GroupRequest groupRequest) {

        groupRepository.save(groupMapper.mapDtoToEntity(groupRequest));

        if(groupRequest.getDepartmentId() != 0L){
            changeDepartment(groupRequest.getId(), groupRequest.getDepartmentId());
        }

        if(groupRequest.getFormOfEducationId() != 0L){
            changeFormOfEducation(groupRequest.getId(), groupRequest.getFormOfEducationId());
        }
    }

    @Override
    public void deleteById(long id) {
        if(groupRepository.findById(id).isPresent()){

            List<Student> groupStudents = studentRepository.findAllByGroupId(id);
            for(Student student : groupStudents){
                studentRepository.leaveGroup(student.getId());
            }

            List<Lesson> groupLesson = lessonRepository.findAllByGroupIdOrderByTimeOfStartLesson(id);
            for(Lesson lesson : groupLesson){
                lessonRepository.removeGroupFromLesson(lesson.getId());
            }

            groupRepository.deleteById(id);
        }
    }

    private void checkThatGroupExist(long groupId){
        if (!groupRepository.findById(groupId).isPresent()) {
            throw new EntityDontExistException("There no group with this id:" + groupId);
        }
    }

    private void checkThatFormOfEducationExist(long formOfEducationId){
        if (!formOfEducationRepository.findById(formOfEducationId).isPresent()) {
            throw new EntityDontExistException("There no formOfEducation with this id:" + formOfEducationId);
        }
    }

    private void checkThatDepartmentExist(long departmentId){
        if (!departmentRepository.findById(departmentId).isPresent()) {
            throw new EntityDontExistException("There no department with this id:" + departmentId);
        }
    }

}
