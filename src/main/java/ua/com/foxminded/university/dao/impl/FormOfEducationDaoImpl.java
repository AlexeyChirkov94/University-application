package ua.com.foxminded.university.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.interfaces.FormOfEducationDao;
import ua.com.foxminded.university.domain.FormOfEducation;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class FormOfEducationDaoImpl extends AbstractPageableCrudDaoImpl<FormOfEducation> implements FormOfEducationDao {

    private static final String SAVE_QUERY = "INSERT INTO formsofeducation (name) VALUES(?)";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name from formsofeducation WHERE id=?";
    private static final String FIND_ALL_NO_PAGES_QUERY = "SELECT id, name FROM formsofeducation ORDER BY id";
    private static final String FIND_ALL_WITH_PAGES_QUERY = "SELECT * FROM formsofeducation order by id offset ? row FETCH NEXT ? ROWS ONLY";
    private static final String UPDATE_QUERY = "UPDATE formsofeducation SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM formsofeducation WHERE id = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) as count from formsofeducation";
    private static final RowMapper<FormOfEducation> ROW_MAPPER = (rs, rowNum) ->
        FormOfEducation.builder()
                .withId(rs.getLong("id"))
                .withName(rs.getString("name"))
                .build();

    public FormOfEducationDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_NO_PAGES_QUERY, UPDATE_QUERY, DELETE_QUERY, ROW_MAPPER,
                FIND_ALL_WITH_PAGES_QUERY, COUNT_QUERY);
    }

    @Override
    protected FormOfEducation insertCertainEntity(FormOfEducation formOfEducation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparePSForInsert(ps, formOfEducation);
            return ps;
        }, keyHolder);

        return FormOfEducation.builder()
                .withId(new Long(String.valueOf(keyHolder.getKeyList().get(0).get("id"))))
                .withName(formOfEducation.getName())
                .build();
    }

    @Override
    protected void preparePSForInsert(PreparedStatement ps, FormOfEducation formOfEducation) throws SQLException {
        ps.setString(1, formOfEducation.getName());
    }

    @Override
    protected int updateCertainEntity(FormOfEducation formOfEducation) {
        return jdbcTemplate.update(UPDATE_QUERY, formOfEducation.getName(), formOfEducation.getId());
    }

    @Override
    protected void preparePSForUpdate(PreparedStatement ps, FormOfEducation formOfEducation) throws SQLException {
        preparePSForInsert(ps, formOfEducation);
        ps.setLong(2, formOfEducation.getId());
    }

}
