package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.mapper.interfaces.GroupResponseMapper;
import static org.assertj.core.api.Assertions.assertThat;

class GroupResponseMapperImplTest {

    ApplicationContext context;
    GroupResponseMapper groupResponseMapper;
    Group group;
    Group emptyGroup;
    GroupResponse groupResponse;
    GroupResponse emptyGroupResponse;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        groupResponseMapper = context.getBean(GroupResponseMapperImpl.class);
        group = Group.builder().withId(1L).withName("Group 1").build();
        emptyGroup = Group.builder().withId(0L).build();
        groupResponse = new GroupResponse();
        groupResponse.setId(1L);
        groupResponse.setName("Group 1");
        emptyGroupResponse = new GroupResponse();
        emptyGroupResponse.setId(0L);
        emptyGroupResponse.setName("");
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntity() {
        GroupResponse expected = groupResponse;
        GroupResponse actual = groupResponseMapper.mapEntityToDto(group);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntityWhichNotFoundedInDB() {
        GroupResponse expected = emptyGroupResponse;
        GroupResponse actual = groupResponseMapper.mapEntityToDto(emptyGroup);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(groupResponseMapper.mapEntityToDto(null)).isNull();
    }

}
