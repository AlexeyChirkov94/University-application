package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.interfaces.FormOfLessonDao;
import ua.com.foxminded.university.dto.FormOfLessonRequest;
import ua.com.foxminded.university.dto.FormOfLessonResponse;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.mapper.interfaces.FormOfLessonRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.FormOfLessonResponseMapper;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.interfaces.FormOfLessonService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FormOfLessonServiceImpl
        extends AbstractPageableCrudService<FormOfLessonRequest, FormOfLessonResponse>
        implements FormOfLessonService {

    private final FormOfLessonDao formOfLessonDao;
    private final FormOfLessonRequestMapper formOfLessonRequestMapper;
    private final FormOfLessonResponseMapper formOfLessonResponseMapper;

    @Override
    public FormOfLessonResponse register(FormOfLessonRequest formOfLessonRequest) {
        if (formOfLessonDao.findByName(formOfLessonRequest.getName()).isPresent()){
            throw new EntityAlreadyExistException("FormOfLesson with same name already exist");
        } else{
            FormOfLesson formOfLessonBeforeSave = formOfLessonRequestMapper.mapDtoToEntity(formOfLessonRequest);
            FormOfLesson formOfLessonAfterSave = formOfLessonDao.save(formOfLessonBeforeSave);

            return formOfLessonResponseMapper.mapEntityToDto(formOfLessonAfterSave);
        }
    }

    @Override
    public Optional<FormOfLessonResponse> findById(long id) {
        return formOfLessonDao.findById(id).map(formOfLessonResponseMapper::mapEntityToDto);
    }

    @Override
    public List<FormOfLessonResponse> findAll(String page) {
        final long itemsCount = formOfLessonDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return formOfLessonDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(formOfLessonResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(FormOfLessonRequest formOfLessonRequest) {
        formOfLessonDao.update(formOfLessonRequestMapper.mapDtoToEntity(formOfLessonRequest));
    }

    @Override
    public boolean deleteById(long id) {
        if(formOfLessonDao.findById(id).isPresent()){
            return formOfLessonDao.deleteById(id);
        }
        return false;
    }
}
