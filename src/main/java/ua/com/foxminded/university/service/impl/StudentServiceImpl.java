package ua.com.foxminded.university.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.interfaces.StudentRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.StudentResponseMapper;
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
    private final StudentRequestMapper studentRequestMapper;
    private final StudentResponseMapper studentResponseMapper;


    public StudentServiceImpl(StudentDao studentDao,GroupDao groupDao, PasswordEncoder passwordEncoder, UserValidator userValidator,
                              StudentRequestMapper studentRequestMapper, StudentResponseMapper studentResponseMapper) {
        super(passwordEncoder, studentDao, userValidator);
        this.studentDao = studentDao;
        this.groupDao = groupDao;
        this.userValidator = userValidator;
        this.studentRequestMapper = studentRequestMapper;
        this.studentResponseMapper = studentResponseMapper;
    }

    @Override
    public Optional<StudentResponse> findById(long id) {
        return studentDao.findById(id).map(studentResponseMapper::mapEntityToDto);
    }

    @Override
    public List<StudentResponse> findAll(String page) {
        final long itemsCount = studentDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return studentDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(studentResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(StudentRequest user) {
        studentDao.update(studentRequestMapper.mapDtoToEntity(user));
    }

    @Override
    public boolean deleteById(long id) {
        if(studentDao.findById(id).isPresent()){
            return studentDao.deleteById(id);
        }
        return false;
    }

    @Override
    public Optional<StudentResponse> findByEmail(String email) {
        return studentDao.findByEmail(email).map(studentResponseMapper::mapEntityToDto);
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
    public boolean enterGroup(long studentId, long groupId) {
        if (studentDao.findById(studentId).isPresent() && groupDao.findById(groupId).isPresent()){
            studentDao.enterGroup(studentId, groupId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected StudentResponse registerCertainUser(StudentRequest userDto) {
        Student studentBeforeSave = studentRequestMapper.mapDtoToEntity(userDto);
        Student studentAfterSave = studentDao.save(studentBeforeSave);

        return studentResponseMapper.mapEntityToDto(studentAfterSave);
    }

}
