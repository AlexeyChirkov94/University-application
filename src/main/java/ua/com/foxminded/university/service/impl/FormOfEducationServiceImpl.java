package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.interfaces.FormOfEducationDao;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationResponseMapper;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.interfaces.FormOfEducationService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FormOfEducationServiceImpl extends AbstractPageableCrudService implements FormOfEducationService {

    private final FormOfEducationDao formOfEducationDao;
    private final FormOfEducationRequestMapper formOfEducationRequestMapper;
    private final FormOfEducationResponseMapper formOfEducationResponseMapper;

    @Override
    public FormOfEducationResponse register(FormOfEducationRequest formOfEducationRequest) {
        if (formOfEducationDao.findByName(formOfEducationRequest.getName()).isPresent()){
            throw new EntityAlreadyExistException("FormOfEducation with same name already exist");
        } else{
            FormOfEducation formOfEducationBeforeSave = formOfEducationRequestMapper.mapDtoToEntity(formOfEducationRequest);
            FormOfEducation formOfEducationAfterSave = formOfEducationDao.save(formOfEducationBeforeSave);

            return formOfEducationResponseMapper.mapEntityToDto(formOfEducationAfterSave);
        }
    }

    @Override
    public Optional<FormOfEducationResponse> findById(long id) {
        return formOfEducationDao.findById(id).map(formOfEducationResponseMapper::mapEntityToDto);
    }

    @Override
    public List<FormOfEducationResponse> findAll(String page) {
        final long itemsCount = formOfEducationDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return formOfEducationDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(formOfEducationResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FormOfEducationResponse> findAll() {

        return formOfEducationDao.findAll().stream()
                .map(formOfEducationResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(FormOfEducationRequest formOfEducationRequest) {
        formOfEducationDao.update(formOfEducationRequestMapper.mapDtoToEntity(formOfEducationRequest));
    }

    @Override
    public boolean deleteById(long id) {
        if(formOfEducationDao.findById(id).isPresent()){
            return formOfEducationDao.deleteById(id);
        }
        return false;
    }

}
