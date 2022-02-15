package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.DepartmentRepository;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.ProfessorRepository;
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
@Transactional
public class DepartmentServiceImpl extends AbstractPageableCrudService implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse create(DepartmentRequest departmentRequest) {
        if (!departmentRepository.findAllByName(departmentRequest.getName()).isEmpty()){
            throw new EntityAlreadyExistException("Department with same name already exist");
        } else {
            Department departmentBeforeSave = departmentMapper.mapDtoToEntity(departmentRequest);
            Department departmentAfterSave = departmentRepository.save(departmentBeforeSave);

            return departmentMapper.mapEntityToDto(departmentAfterSave);
        }
    }

    @Override
    public DepartmentResponse findById(long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no department with id: " + id));

        return departmentMapper.mapEntityToDto(department);
    }

    @Override
    public List<DepartmentResponse> findAll(String page) {
        final long itemsCount = departmentRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return departmentRepository.findAll(PageRequest.of(pageNumber - 1, ITEMS_PER_PAGE, Sort.by("id")))
                .stream().map(departmentMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponse> findAll() {

        return departmentRepository.findAll(Sort.by("id")).stream()
                .map(departmentMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(DepartmentRequest departmentRequest) {
        departmentRepository.save(departmentMapper.mapDtoToEntity(departmentRequest));
    }

    @Override
    public void deleteById(long id) {
        if(departmentRepository.findById(id).isPresent()){

            List<Professor> departmentsProfessors = professorRepository.findAllByDepartmentId(id);
            for (Professor professor : departmentsProfessors){
                professorRepository.removeDepartmentFromProfessor(professor.getId());
            }

            List<Course> departmentsCourses = courseRepository.findByDepartmentId(id);
            for(Course course : departmentsCourses){
                courseRepository.removeDepartmentFromCourse(course.getId());
            }

            List<Group> departmentsGroup = groupRepository.findAllByDepartmentIdOrderById(id);
            for (Group group : departmentsGroup){
                groupRepository.removeDepartmentFromGroup(group.getId());
            }

            departmentRepository.deleteById(id);
        }
    }

}
