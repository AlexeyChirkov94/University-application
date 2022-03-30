package ua.com.foxminded.university.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.RoleRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.Role;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.StudentMapper;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.StudentService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl extends AbstractUserServiceImpl<StudentRequest, StudentResponse> implements StudentService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentRepository studentRepository, GroupRepository groupRepository, PasswordEncoder passwordEncoder,
                              StudentMapper studentMapper, RoleRepository roleRepository) {
        super(passwordEncoder, studentRepository, roleRepository);
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public StudentResponse findById(long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no student with id: " + id));

        return studentMapper.mapEntityToDto(student);
    }

    @Override
    public List<StudentResponse> findAll(String page) {
        final long itemsCount = studentRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return studentRepository.findAll(PageRequest.of(pageNumber - 1, ITEMS_PER_PAGE))
                .stream().map(studentMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponse> findAll() {

        return studentRepository.findAll().stream()
                .map(studentMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponse> findByGroupId(long groupId) {

        return studentRepository.findAllByGroupId(groupId).stream()
                .map(studentMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(StudentRequest studentRequest) {
        studentRequest.setPassword(passwordEncoder.encode(studentRequest.getPassword()));

        studentRepository.save(studentMapper.mapDtoToEntity(studentRequest));

        if(studentRequest.getGroupId() != 0L){
            changeGroup(studentRequest.getId(), studentRequest.getGroupId());
        }

    }

    @Override
    public void deleteById(long id) {
        if(studentRepository.findById(id).isPresent()){
            removeAllRolesFromUser(id);
            studentRepository.deleteById(id);
        }
    }

    @Override
    public List<StudentResponse> findByEmail(String email) {
        List<Student> students = studentRepository.findAllByEmail(email);

        if(!students.isEmpty()){
            StudentResponse studentResponse = studentMapper.mapEntityToDto(students.get(0));
            studentResponse.setRoles(roleRepository.findAllByUserId(studentResponse.getId()));
            return Arrays.asList(studentResponse);
        }

        return Collections.emptyList();
    }

    @Override
    public boolean leaveGroup(long studentId) {
        if (studentRepository.findById(studentId).isPresent()){
            studentRepository.leaveGroup(studentId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean changeGroup(long studentId, long groupId) {
        if (studentRepository.findById(studentId).isPresent() && groupRepository.findById(groupId).isPresent()){
            studentRepository.enterGroup(studentId, groupId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected StudentResponse registerCertainUser(StudentRequest studentRequest){
        Student studentBeforeSave = studentMapper.mapDtoToEntity(studentRequest);
        Student studentAfterSave = studentRepository.save(studentBeforeSave);
        long studentId = studentAfterSave.getId();

        if(studentRequest.getGroupId() != 0L){
            changeGroup(studentId, studentRequest.getGroupId());
        }

        if(roleRepository.findAllByUserId(studentId).isEmpty()) {

            List<Role> studentRoles = roleRepository.findAllByName("ROLE_STUDENT");

            if (studentRoles.isEmpty()){
                throw new EntityDontExistException("ROLE_STUDENT not initialized");
            }

            addRoleToUser(studentId, studentRoles.get(0).getId());
        }

        return studentMapper.mapEntityToDto(studentAfterSave);
    }

}
