package ua.com.foxminded.university.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.RoleDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.Role;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.StudentMapper;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.interfaces.StudentService;
import ua.com.foxminded.university.service.validator.UserValidator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl extends AbstractUserServiceImpl<StudentRequest, StudentResponse> implements StudentService {

    private final StudentDao studentDao;
    private final GroupDao groupDao;
    private final UserValidator userValidator;
    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentDao studentDao,GroupDao groupDao, PasswordEncoder passwordEncoder, UserValidator userValidator,
                              StudentMapper studentMapper, RoleDao roleDao) {
        super(passwordEncoder, studentDao, roleDao, userValidator);
        this.studentDao = studentDao;
        this.groupDao = groupDao;
        this.userValidator = userValidator;
        this.studentMapper = studentMapper;
    }

    @Override
    public StudentResponse findById(long id) {
        Student student = studentDao.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no student with id: " + id));

        return studentMapper.mapEntityToDto(student);
    }

    @Override
    public List<StudentResponse> findAll(String page) {
        final long itemsCount = studentDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return studentDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(studentMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponse> findAll() {

        return studentDao.findAll().stream()
                .map(studentMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponse> findByGroupId(long groupId) {

        return studentDao.findByGroupId(groupId).stream()
                .map(studentMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public void edit(StudentRequest studentRequest) {
        userValidator.validate(studentRequest);
        studentRequest.setPassword(passwordEncoder.encode(studentRequest.getPassword()));

        studentDao.update(studentMapper.mapDtoToEntity(studentRequest));

        if(studentRequest.getGroupId() != 0L){
            changeGroup(studentRequest.getId(), studentRequest.getGroupId());
        }

    }

    @Override
    public boolean deleteById(long id) {
        if(studentDao.findById(id).isPresent()){
            removeAllRolesFromUser(id);
            return studentDao.deleteById(id);
        }
        return false;
    }

    @Override
    public Optional<StudentResponse> findByEmail(String email) {
        Optional<Student> studentOptional = studentDao.findByEmail(email);

        if(studentOptional.isPresent()){
            StudentResponse studentResponse = studentMapper.mapEntityToDto(studentOptional.get());
            studentResponse.setRoles(roleDao.findByUserId(studentResponse.getId()));
            return Optional.of(studentResponse);
        }

        return studentOptional.map(studentMapper::mapEntityToDto);
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public boolean leaveGroup(long studentId) {
        if (studentDao.findById(studentId).isPresent()){
            studentDao.leaveGroup(studentId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public boolean changeGroup(long studentId, long groupId) {
        if (studentDao.findById(studentId).isPresent() && groupDao.findById(groupId).isPresent()){
            studentDao.enterGroup(studentId, groupId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(transactionManager = "txManager")
    protected StudentResponse registerCertainUser(StudentRequest studentRequest) {
        Student studentBeforeSave = studentMapper.mapDtoToEntity(studentRequest);
        Student studentAfterSave = studentDao.save(studentBeforeSave);
        long studentId = studentAfterSave.getId();

        if(studentRequest.getGroupId() != 0L){
            changeGroup(studentId, studentRequest.getGroupId());
        }

        if(roleDao.findByUserId(studentId).size() == 0) {
            Role studentRole = roleDao.findByName("ROLE_STUDENT")
                    .orElseThrow(() -> new EntityDontExistException("ROLE_STUDENT not initialized"));

            addRoleToUser(studentId, studentRole.getId());
        }

        return studentMapper.mapEntityToDto(studentAfterSave);
    }

}
