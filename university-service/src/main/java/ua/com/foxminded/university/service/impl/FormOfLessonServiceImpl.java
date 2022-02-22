package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.repository.FormOfLessonRepository;
import ua.com.foxminded.university.repository.LessonRepository;
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
@Transactional
public class FormOfLessonServiceImpl extends AbstractPageableCrudService implements FormOfLessonService {

    private final FormOfLessonRepository formOfLessonRepository;
    private final LessonRepository lessonRepository;
    private final FormOfLessonMapper formOfLessonMapper;

    @Override
    public FormOfLessonResponse create(FormOfLessonRequest formOfLessonRequest) {
        if (!formOfLessonRepository.findAllByName(formOfLessonRequest.getName()).isEmpty()){
            throw new EntityAlreadyExistException("Form of lesson with same name already exist");
        } else{
            FormOfLesson formOfLessonBeforeSave = formOfLessonMapper.mapDtoToEntity(formOfLessonRequest);
            FormOfLesson formOfLessonAfterSave = formOfLessonRepository.save(formOfLessonBeforeSave);

            return formOfLessonMapper.mapEntityToDto(formOfLessonAfterSave);
        }
    }

    @Override
    public FormOfLessonResponse findById(long id) {
        FormOfLesson formOfLesson = formOfLessonRepository.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no form of lesson with id: " + id));

        return formOfLessonMapper.mapEntityToDto(formOfLesson);
    }

    @Override
    public List<FormOfLessonResponse> findAll(String page) {
        final long itemsCount = formOfLessonRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return formOfLessonRepository.findAll(PageRequest.of(pageNumber - 1, ITEMS_PER_PAGE, Sort.by("id"))).stream()
                .map(formOfLessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FormOfLessonResponse> findAll() {

        return formOfLessonRepository.findAll(Sort.by("id")).stream()
                .map(formOfLessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(FormOfLessonRequest formOfLessonRequest) {
        formOfLessonRepository.save(formOfLessonMapper.mapDtoToEntity(formOfLessonRequest));
    }

    @Override
    public void deleteById(long id) {
        if(formOfLessonRepository.findById(id).isPresent()){

            List<Lesson> formOfLessonLessons = lessonRepository.findAllByFormOfLessonIdOrderByTimeOfStartLesson(id);
            for(Lesson lesson : formOfLessonLessons){
                lessonRepository.removeFormOfLessonFromLesson(lesson.getId());
            }

            formOfLessonRepository.deleteById(id);
        }
    }

}
