package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.FormOfLessonDao;
import ua.com.foxminded.university.entity.FormOfLesson;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Repository
@Log4j
public class FormOfLessonDaoImpl extends AbstractPageableCrudDaoImpl<FormOfLesson> implements FormOfLessonDao {

    private static final String SAVE_QUERY = "INSERT INTO formsOfLesson (name, durationOfLesson) VALUES(?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name, durationOfLesson from formsOfLesson WHERE id=?";
    private static final String FIND_BY_NAME_QUERY = "SELECT id, name, durationOfLesson from formsOfLesson WHERE name=?";
    private static final String FIND_ALL_NO_PAGES_QUERY = "SELECT id, name, durationOfLesson FROM formsOfLesson ORDER BY id";
    private static final String FIND_ALL_WITH_PAGES_QUERY = "SELECT * FROM formsOfLesson order by id offset ? row FETCH NEXT ? ROWS ONLY";
    private static final String UPDATE_QUERY = "UPDATE formsOfLesson SET name = ?, durationOfLesson = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM formsOfLesson WHERE id = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) as count from formsOfLesson";
    private static final RowMapper<FormOfLesson> ROW_MAPPER = (rs, rowNum) ->
        FormOfLesson.builder()
                .withId(rs.getLong("id"))
                .withName(rs.getString("name"))
                .withDuration(rs.getInt("durationOfLesson"))
                .build();

    public FormOfLessonDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_NO_PAGES_QUERY, UPDATE_QUERY, DELETE_QUERY, ROW_MAPPER,
                FIND_ALL_WITH_PAGES_QUERY, COUNT_QUERY);
    }

    @Override
    public Optional<FormOfLesson> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, ROW_MAPPER, name));
        } catch (DataAccessException e) {
            log.info("FormOfLesson with this name not registered, Name: " + name);
            return Optional.empty();
        }
    }

    @Override
    protected FormOfLesson insertCertainEntity(FormOfLesson formOflesson) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparePreparedStatementForInsert(ps, formOflesson);
            return ps;
        }, keyHolder);

        return FormOfLesson.builder()
                .withId(getIdOfSavedEntity(keyHolder))
                .withName(formOflesson.getName())
                .withDuration(formOflesson.getDuration())
                .build();
    }

    @Override
    protected void preparePreparedStatementForInsert(PreparedStatement ps, FormOfLesson formOfEducation) throws SQLException {
        ps.setString(1, formOfEducation.getName());
        ps.setInt(2, formOfEducation.getDuration());
    }

    @Override
    protected int updateCertainEntity(FormOfLesson formOfEducation) {
        return jdbcTemplate.update(UPDATE_QUERY, formOfEducation.getName(), formOfEducation.getDuration(),
                formOfEducation.getId());
    }

    @Override
    protected void preparePreparedStatementForUpdate(PreparedStatement ps, FormOfLesson formOflesson) throws SQLException {
        preparePreparedStatementForInsert(ps, formOflesson);
        ps.setLong(3, formOflesson.getId());
    }

}
