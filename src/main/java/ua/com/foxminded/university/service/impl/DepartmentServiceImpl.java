package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.mapper.interfaces.DepartmentRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.DepartmentResponseMapper;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.interfaces.DepartmentService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl extends AbstractPageableCrudService implements DepartmentService {

    private final DepartmentDao departmentDao;
    private final ProfessorDao professorDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final DepartmentRequestMapper departmentRequestMapper;
    private final DepartmentResponseMapper departmentResponseMapper;

    @Override
    public DepartmentResponse create(DepartmentRequest departmentRequest) {
        if (departmentDao.findByName(departmentRequest.getName()).isPresent()){
            throw new EntityAlreadyExistException("Department with same name already exist");
        } else {
            Department departmentBeforeSave = departmentRequestMapper.mapDtoToEntity(departmentRequest);
            Department departmentAfterSave = departmentDao.save(departmentBeforeSave);

            return departmentResponseMapper.mapEntityToDto(departmentAfterSave);
        }
    }

    @Override
    public DepartmentResponse findById(long id) {
        Department department = departmentDao.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no department with id: " + id));

        return departmentResponseMapper.mapEntityToDto(department);
    }

    @Override
    public List<DepartmentResponse> findAll(String page) {
        final long itemsCount = departmentDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return departmentDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(departmentResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponse> findAll() {

        return departmentDao.findAll().stream()
                .map(departmentResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(DepartmentRequest departmentRequest) {
        departmentDao.update(departmentRequestMapper.mapDtoToEntity(departmentRequest));
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public boolean deleteById(long id) {
        if(departmentDao.findById(id).isPresent()){

            List<Professor> departmentsProfessors = professorDao.findByDepartmentId(id);
            for (Professor professor : departmentsProfessors){
                professorDao.removeDepartmentFromProfessor(professor.getId());
            }

            List<Course> departmentsCourses = courseDao.findByDepartmentId(id);
            for(Course course : departmentsCourses){
                courseDao.removeDepartmentFromCourse(course.getId());
            }

            List<Group> departmentsGroup = groupDao.findByDepartmentId(id);
            for (Group group : departmentsGroup){
                groupDao.removeDepartmentFromGroup(group.getId());
            }

            return departmentDao.deleteById(id);
        }
        return false;
    }

}
