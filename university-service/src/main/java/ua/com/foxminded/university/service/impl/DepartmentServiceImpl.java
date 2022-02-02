package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.DepartmentDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.ProfessorDao;
import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.mapper.DepartmentMapper;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.DepartmentService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(transactionManager = "hibernateTransactionManager")
public class DepartmentServiceImpl extends AbstractPageableCrudService implements DepartmentService {

    private final DepartmentDao departmentDao;
    private final ProfessorDao professorDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse create(DepartmentRequest departmentRequest) {
        if (!departmentDao.findByName(departmentRequest.getName()).isEmpty()){
            throw new EntityAlreadyExistException("Department with same name already exist");
        } else {
            Department departmentBeforeSave = departmentMapper.mapDtoToEntity(departmentRequest);
            Department departmentAfterSave = departmentDao.save(departmentBeforeSave);

            return departmentMapper.mapEntityToDto(departmentAfterSave);
        }
    }

    @Override
    public DepartmentResponse findById(long id) {
        Department department = departmentDao.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no department with id: " + id));

        return departmentMapper.mapEntityToDto(department);
    }

    @Override
    public List<DepartmentResponse> findAll(String page) {
        final long itemsCount = departmentDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return departmentDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(departmentMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponse> findAll() {

        return departmentDao.findAll().stream()
                .map(departmentMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(DepartmentRequest departmentRequest) {
        departmentDao.update(departmentMapper.mapDtoToEntity(departmentRequest));
    }

    @Override
    public void deleteById(long id) {
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

            departmentDao.deleteById(id);
        }
    }

}
