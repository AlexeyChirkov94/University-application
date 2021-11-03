package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.mapper.interfaces.DepartmentRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.DepartmentResponseMapper;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.interfaces.DepartmentService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl extends AbstractPageableCrudService implements DepartmentService {

    private final DepartmentDao departmentDao;
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
    public Optional<DepartmentResponse> findById(long id) {
        return departmentDao.findById(id).map(departmentResponseMapper::mapEntityToDto);
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
    public boolean deleteById(long id) {
        if(departmentDao.findById(id).isPresent()){
            return departmentDao.deleteById(id);
        }
        return false;
    }

}
