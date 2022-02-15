package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.repository.FormOfEducationRepository;
import ua.com.foxminded.university.repository.GroupRepository;
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

    private final FormOfEducationRepository formOfEducationRepository;
    private final GroupRepository groupRepository;
    private final FormOfEducationMapper formOfEducationMapper;

    @Override
    public FormOfEducationResponse create(FormOfEducationRequest formOfEducationRequest) {
        if (!formOfEducationRepository.findAllByName(formOfEducationRequest.getName()).isEmpty()){
            throw new EntityAlreadyExistException("Form of education with same name already exist");
        } else{
            FormOfEducation formOfEducationBeforeSave = formOfEducationMapper.mapDtoToEntity(formOfEducationRequest);
            FormOfEducation formOfEducationAfterSave = formOfEducationRepository.save(formOfEducationBeforeSave);

            return formOfEducationMapper.mapEntityToDto(formOfEducationAfterSave);
        }
    }

    @Override
    public FormOfEducationResponse findById(long id) {
        FormOfEducation formOfEducation = formOfEducationRepository.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no form of education with id: " + id));

        return formOfEducationMapper.mapEntityToDto(formOfEducation);
    }

    @Override
    public List<FormOfEducationResponse> findAll(String page) {
        final long itemsCount = formOfEducationRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return formOfEducationRepository.findAll(PageRequest.of(pageNumber - 1, ITEMS_PER_PAGE, Sort.by("id"))).stream()
                .map(formOfEducationMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FormOfEducationResponse> findAll() {

        return formOfEducationRepository.findAll(Sort.by("id")).stream()
                .map(formOfEducationMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(FormOfEducationRequest formOfEducationRequest) {
        formOfEducationRepository.save(formOfEducationMapper.mapDtoToEntity(formOfEducationRequest));
    }

    @Override
    public void deleteById(long id) {
        if(formOfEducationRepository.findById(id).isPresent()){

            List<Group> formOfEducationGroups = groupRepository.findAllByFormOfEducationIdOrderById(id);
            for(Group group : formOfEducationGroups){
                groupRepository.removeFormOfEducationFromGroup(group.getId());
            }

            formOfEducationRepository.deleteById(id);
        }
    }

}
