package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.FormOfLessonDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.dto.FormOfLessonRequest;
import ua.com.foxminded.university.dto.FormOfLessonResponse;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.mapper.FormOfLessonMapper;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.FormOfLessonService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(transactionManager = "hibernateTransactionManager")
public class FormOfLessonServiceImpl extends AbstractPageableCrudService implements FormOfLessonService {

    private final FormOfLessonDao formOfLessonDao;
    private final LessonDao lessonDao;
    private final FormOfLessonMapper formOfLessonMapper;

    @Override
    public FormOfLessonResponse create(FormOfLessonRequest formOfLessonRequest) {
        if (!formOfLessonDao.findByName(formOfLessonRequest.getName()).isEmpty()){
            throw new EntityAlreadyExistException("Form of lesson with same name already exist");
        } else{
            FormOfLesson formOfLessonBeforeSave = formOfLessonMapper.mapDtoToEntity(formOfLessonRequest);
            FormOfLesson formOfLessonAfterSave = formOfLessonDao.save(formOfLessonBeforeSave);

            return formOfLessonMapper.mapEntityToDto(formOfLessonAfterSave);
        }
    }

    @Override
    public FormOfLessonResponse findById(long id) {
        FormOfLesson formOfLesson = formOfLessonDao.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no form of lesson with id: " + id));

        return formOfLessonMapper.mapEntityToDto(formOfLesson);
    }

    @Override
    public List<FormOfLessonResponse> findAll(String page) {
        final long itemsCount = formOfLessonDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return formOfLessonDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(formOfLessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FormOfLessonResponse> findAll() {

        return formOfLessonDao.findAll().stream()
                .map(formOfLessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(FormOfLessonRequest formOfLessonRequest) {
        formOfLessonDao.update(formOfLessonMapper.mapDtoToEntity(formOfLessonRequest));
    }

    @Override
    public void deleteById(long id) {
        if(formOfLessonDao.findById(id).isPresent()){

            List<Lesson> formOfLessonLessons = lessonDao.findByFormOfLessonId(id);
            for(Lesson lesson : formOfLessonLessons){
                lessonDao.removeFormOfLessonFromLesson(lesson.getId());
            }

            formOfLessonDao.deleteById(id);
        }
    }

}
