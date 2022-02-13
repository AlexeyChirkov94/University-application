package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.FormOfEducationDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.mapper.FormOfEducationMapper;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.FormOfEducationService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class FormOfEducationServiceImpl extends AbstractPageableCrudService implements FormOfEducationService {

    private final FormOfEducationDao formOfEducationDao;
    private final GroupDao groupDao;
    private final FormOfEducationMapper formOfEducationMapper;

    @Override
    public FormOfEducationResponse create(FormOfEducationRequest formOfEducationRequest) {
        if (!formOfEducationDao.findByName(formOfEducationRequest.getName()).isEmpty()){
            throw new EntityAlreadyExistException("Form of education with same name already exist");
        } else{
            FormOfEducation formOfEducationBeforeSave = formOfEducationMapper.mapDtoToEntity(formOfEducationRequest);
            FormOfEducation formOfEducationAfterSave = formOfEducationDao.save(formOfEducationBeforeSave);

            return formOfEducationMapper.mapEntityToDto(formOfEducationAfterSave);
        }
    }

    @Override
    public FormOfEducationResponse findById(long id) {
        FormOfEducation formOfEducation = formOfEducationDao.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no form of education with id: " + id));

        return formOfEducationMapper.mapEntityToDto(formOfEducation);
    }

    @Override
    public List<FormOfEducationResponse> findAll(String page) {
        final long itemsCount = formOfEducationDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return formOfEducationDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(formOfEducationMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FormOfEducationResponse> findAll() {

        return formOfEducationDao.findAll().stream()
                .map(formOfEducationMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(FormOfEducationRequest formOfEducationRequest) {
        formOfEducationDao.update(formOfEducationMapper.mapDtoToEntity(formOfEducationRequest));
    }

    @Override
    public void deleteById(long id) {
        if(formOfEducationDao.findById(id).isPresent()){

            List<Group> formOfEducationGroups = groupDao.findByFormOfEducation(id);
            for(Group group : formOfEducationGroups){
                groupDao.removeFormOfEducationFromGroup(group.getId());
            }

            formOfEducationDao.deleteById(id);
        }
    }

}
