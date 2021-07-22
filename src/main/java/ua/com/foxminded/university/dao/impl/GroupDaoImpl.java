package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.entity.Group;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Repository
@Log4j
public class GroupDaoImpl extends AbstractPageableCrudDaoImpl<Group> implements GroupDao {

    private static final String FIND_QUERY = "SELECT g.id, g.name, g.department_id, " +
            "d.name as department_name, g.formOfEducation_id, f.name as formOfEducation_name " +
            "from groups g left join departments d on g.department_id=d.id " +
            "left join formsofeducation f on g.formofeducation_id = f.id ";
    private static final String SAVE_QUERY = "INSERT INTO groups (name) VALUES(?)";
    private static final String FIND_BY_ID_QUERY = FIND_QUERY + "WHERE g.id=?";
    private static final String FIND_BY_NAME_QUERY = FIND_QUERY + "WHERE g.name=?";
    private static final String FIND_ALL_NO_PAGES_QUERY = FIND_QUERY + "ORDER BY g.id";
    private static final String FIND_ALL_WITH_PAGES_QUERY = FIND_QUERY + "order by g.id offset ? row FETCH NEXT ? ROWS ONLY";
    private static final String UPDATE_QUERY = "UPDATE groups SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM groups WHERE id = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) as count from groups";
    private static final String CHANGE_FORM_OF_EDUCATION_QUERY = "UPDATE groups SET formOfEducation_id = ? WHERE id = ?";
    private static final RowMapper<Group> ROW_MAPPER = (rs, rowNum) ->
        Group.builder()
                .withId(rs.getLong("id"))
                .withName(rs.getString("name"))
                .withDepartment(Department.builder()
                        .withId(rs.getLong("department_id"))
                        .withName(rs.getString("department_name"))
                        .build())
                .withFormOfEducation(FormOfEducation.builder()
                        .withId(rs.getLong("formOfEducation_id"))
                        .withName(rs.getString("formOfEducation_name"))
                        .build())
                .build();

    public GroupDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_NO_PAGES_QUERY, UPDATE_QUERY, DELETE_QUERY, ROW_MAPPER,
                FIND_ALL_WITH_PAGES_QUERY, COUNT_QUERY);
    }

    @Override
    public Optional<Group> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, ROW_MAPPER, name));
        } catch (DataAccessException e) {
            log.info("Department with this name not registered, Name: " + name);
            return Optional.empty();
        }
    }

    @Override
    public void changeFormOfEducation(long groupId, long newFormOfEducationId) {
        jdbcTemplate.update(CHANGE_FORM_OF_EDUCATION_QUERY, newFormOfEducationId, groupId);
    }

    @Override
    protected Group insertCertainEntity(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparePreparedStatementForInsert(ps, group);
            return ps;
        }, keyHolder);

        return Group.builder()
                .withId(getIdOfSavedEntity(keyHolder))
                .withName(group.getName())
                .build();
    }

    @Override
    protected void preparePreparedStatementForInsert(PreparedStatement ps, Group group) throws SQLException {
        ps.setString(1, group.getName());
    }

    @Override
    protected int updateCertainEntity(Group group) {
        return jdbcTemplate.update(UPDATE_QUERY, group.getName(), group.getId());
    }

    @Override
    protected void preparePreparedStatementForUpdate(PreparedStatement ps, Group group) throws SQLException {
        preparePreparedStatementForInsert(ps, group);;
        ps.setLong(2, group.getId());
    }

}
