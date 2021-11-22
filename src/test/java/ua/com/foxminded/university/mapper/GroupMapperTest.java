package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.entity.Group;
import static org.assertj.core.api.Assertions.assertThat;

class GroupMapperTest {

    ApplicationContext context;
    GroupMapper groupMapper;
    Group group;
    Group emptyGroup;
    GroupResponse groupResponse;
    GroupRequest groupRequest;
    GroupResponse emptyGroupResponse;


    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        groupMapper = context.getBean(GroupMapper.class);

        group = Group.builder().withId(1L).withName("Group 1").build();
        emptyGroup = Group.builder().build();

        groupRequest = new GroupRequest();
        groupRequest.setId(1L);
        groupRequest.setName("Group 1");

        groupResponse = new GroupResponse();
        groupResponse.setId(1L);
        groupResponse.setName("Group 1");

        emptyGroupResponse = new GroupResponse();
        emptyGroupResponse.setId(0L);
        emptyGroupResponse.setName("");
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsGroupEntity() {
        GroupResponse expected = groupResponse;
        GroupResponse actual = groupMapper.mapEntityToDto(group);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsGroupEntityWhichNotFoundedInDB() {
        GroupResponse expected = emptyGroupResponse;
        GroupResponse actual = groupMapper.mapEntityToDto(emptyGroup);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(groupMapper.mapEntityToDto(null)).isNull();
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsGroupRequestDto() {
        Group expected = group;
        Group actual = groupMapper.mapDtoToEntity(groupRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(groupMapper.mapDtoToEntity(null)).isNull();
    }

}
