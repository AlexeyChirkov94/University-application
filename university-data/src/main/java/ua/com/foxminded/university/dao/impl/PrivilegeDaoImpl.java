package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.PrivilegeDao;
import ua.com.foxminded.university.entity.Privilege;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@Log4j
public class PrivilegeDaoImpl extends AbstractPageableCrudDaoImpl<Privilege> implements PrivilegeDao {

    private static final String SAVE_QUERY = "INSERT INTO privileges (name) VALUES(?)";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name from privileges WHERE id=?";
    private static final String FIND_BY_NAME_QUERY = "SELECT id, name from privileges WHERE name=?";
    private static final String FIND_BY_ROLE_ID = "SELECT p.id, p.name from privileges p " +
            "left join role_privilege rp on p.id = rp.privilege_id " +
            "left join usersroles r on rp.role_id = r.id where r.id = ?";
    private static final String FIND_ALL_NO_PAGES_QUERY = "SELECT id, name FROM privileges ORDER BY id";
    private static final String FIND_ALL_WITH_PAGES_QUERY = "SELECT * FROM privileges order by id offset ? row FETCH NEXT ? ROWS ONLY";
    private static final String UPDATE_QUERY = "UPDATE privileges SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM privileges WHERE id = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) as count from privileges";
    private static final RowMapper<Privilege> ROW_MAPPER = (rs, rowNum) ->
            Privilege.builder()
                    .withId(rs.getLong("id"))
                    .withName(rs.getString("name"))
                    .build();

    public PrivilegeDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_NO_PAGES_QUERY, UPDATE_QUERY, DELETE_QUERY, ROW_MAPPER,
                FIND_ALL_WITH_PAGES_QUERY, COUNT_QUERY);
    }

    @Override
    public Optional<Privilege> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, ROW_MAPPER, name));
        } catch (DataAccessException e) {
            log.info("role with name: " + name + " not registered");
            return Optional.empty();
        }
    }

    @Override
    public List<Privilege> findByRoleId(long roleId) {
        return jdbcTemplate.query(FIND_BY_ROLE_ID, ROW_MAPPER, roleId);
    }

    @Override
    protected Privilege insertCertainEntity(Privilege entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparePreparedStatementForInsert(ps, entity);
            return ps;
        }, keyHolder);


        return Privilege.builder()
                .withId(getIdOfSavedEntity(keyHolder))
                .withName(entity.getName())
                .build();
    }

    @Override
    protected void preparePreparedStatementForInsert(PreparedStatement ps, Privilege entity) throws SQLException {
        ps.setString(1, entity.getName());
    }

    @Override
    protected int updateCertainEntity(Privilege entity) {
        return jdbcTemplate.update(UPDATE_QUERY, entity.getName(), entity.getId());
    }

    @Override
    protected void preparePreparedStatementForUpdate(PreparedStatement ps, Privilege entity) throws SQLException {
        preparePreparedStatementForInsert(ps, entity);
        ps.setLong(2, entity.getId());
    }

}
