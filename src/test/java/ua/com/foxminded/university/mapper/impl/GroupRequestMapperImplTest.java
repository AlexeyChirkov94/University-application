package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.mapper.interfaces.GroupRequestMapper;
import static org.assertj.core.api.Assertions.assertThat;

class GroupRequestMapperImplTest {

    ApplicationContext context;
    GroupRequestMapper groupRequestMapper;
    Group group;
    GroupRequest groupRequest;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        groupRequestMapper = context.getBean(GroupRequestMapperImpl.class);
        group = Group.builder().withId(1L).withName("Group 1").build();
        groupRequest = new GroupRequest();
        groupRequest.setId(1L);
        groupRequest.setName("Group 1");
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfLessonRequestDto() {
        Group expected = group;
        Group actual = groupRequestMapper.mapDtoToEntity(groupRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(groupRequestMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntity() {
        GroupRequest expected = groupRequest;
        GroupRequest actual = groupRequestMapper.mapEntityToDto(group);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(groupRequestMapper.mapEntityToDto(null)).isNull();
    }

}