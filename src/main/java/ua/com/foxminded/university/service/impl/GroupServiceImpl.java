package ua.com.foxminded.university.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.interfaces.FormOfEducationDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.mapper.interfaces.GroupRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.GroupResponseMapper;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.interfaces.GroupService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl
        extends AbstractPageableCrudService<GroupRequest, GroupResponse>
        implements GroupService {

    private final GroupDao groupDao;
    private final FormOfEducationDao formOfEducationDao;
    private final GroupRequestMapper groupRequestMapper;
    private final GroupResponseMapper groupResponseMapper;

    public GroupServiceImpl(GroupDao groupDao, FormOfEducationDao formOfEducationDao,
                            GroupRequestMapper groupRequestMapper, GroupResponseMapper groupResponseMapper) {
        this.groupDao = groupDao;
        this.formOfEducationDao = formOfEducationDao;
        this.groupRequestMapper = groupRequestMapper;
        this.groupResponseMapper = groupResponseMapper;
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public void changeFormOfEducation(long groupId, long newFormOfEducationId) {
        checkThatGroupAndFormOfLessonExist(groupId, newFormOfEducationId);
        groupDao.changeFormOfEducation(groupId, newFormOfEducationId);
    }

    @Override
    public GroupResponse register(GroupRequest groupRequest) {
        if (groupDao.findByName(groupRequest.getName()).isPresent()){
            throw new EntityAlreadyExistException("Group with same name already exist");
        } else {
            Group groupBeforeSave = groupRequestMapper.mapDtoToEntity(groupRequest);
            Group groupAfterSave = groupDao.save(groupBeforeSave);

            return groupResponseMapper.mapEntityToDto(groupAfterSave);
        }
    }

    @Override
    public Optional<GroupResponse> findById(long id) {
        return groupDao.findById(id).map(groupResponseMapper::mapEntityToDto);
    }

    @Override
    public List<GroupResponse> findAll(String page) {
        final long itemsCount = groupDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return groupDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(groupResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(GroupRequest groupRequest) {
        groupDao.update(groupRequestMapper.mapDtoToEntity(groupRequest));
    }

    @Override
    public boolean deleteById(long id) {
        if(groupDao.findById(id).isPresent()){
            return groupDao.deleteById(id);
        }
        return false;
    }

    private void checkThatGroupAndFormOfLessonExist(long groupId, long newFormOfEducationId){
        if (!groupDao.findById(groupId).isPresent() || !formOfEducationDao.findById(newFormOfEducationId).isPresent()) {
            throw new EntityDontExistException("There no group or FormOfEducation with this ids");
        }
    }

}
