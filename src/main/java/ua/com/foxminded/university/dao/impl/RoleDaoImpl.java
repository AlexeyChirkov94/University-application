package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.interfaces.RoleDao;
import ua.com.foxminded.university.entity.Role;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@Log4j
public class RoleDaoImpl extends AbstractPageableCrudDaoImpl<Role> implements RoleDao {

    private static final String SAVE_QUERY = "INSERT INTO usersroles (name) VALUES(?)";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name from usersroles WHERE id=?";
    private static final String FIND_BY_NAME_QUERY = "SELECT id, name from usersroles WHERE name=?";
    private static final String FIND_BY_USER_ID = "SELECT r.id, r.name from usersroles r " +
            "left join user_role ur on r.id = ur.role_id " +
            "left join users u on ur.user_id = u.id where u.id = ?";
    private static final String FIND_ALL_NO_PAGES_QUERY = "SELECT id, name FROM usersroles ORDER BY id";
    private static final String FIND_ALL_WITH_PAGES_QUERY = "SELECT * FROM usersroles order by id offset ? row FETCH NEXT ? ROWS ONLY";
    private static final String UPDATE_QUERY = "UPDATE usersroles SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM usersroles WHERE id = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) as count from usersroles";
    private static final String ADD_PRIVILEGE_TO_ROLE_QUERY = "INSERT INTO role_privilege (role_id, privilege_id) VALUES(?, ?)";
    private static final String REMOVE_PRIVILEGE_FROM_ROLE_QUERY = "DELETE FROM role_privilege WHERE role_id = ? AND privilege_id = ?";
    private static final RowMapper<Role> ROW_MAPPER = (rs, rowNum) ->
            Role.builder()
                    .withId(rs.getLong("id"))
                    .withName(rs.getString("name"))
                    .build();

    public RoleDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_NO_PAGES_QUERY, UPDATE_QUERY, DELETE_QUERY, ROW_MAPPER,
                FIND_ALL_WITH_PAGES_QUERY, COUNT_QUERY);
    }

    @Override
    public Optional<Role> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, ROW_MAPPER, name));
        } catch (DataAccessException e) {
            log.info("role with name: " + name + " not registered");
            return Optional.empty();
        }
    }

    @Override
    public List<Role> findByUserId(long roleId) {
        return jdbcTemplate.query(FIND_BY_USER_ID, ROW_MAPPER, roleId);
    }

    @Override
    public void addPrivilegeToRole(long roleId, long addingPrivilegeId) {
        jdbcTemplate.update(ADD_PRIVILEGE_TO_ROLE_QUERY, roleId, addingPrivilegeId);
    }

    @Override
    public void removePrivilegeFromRole(long roleId, long removingPrivilegeId) {
        jdbcTemplate.update(REMOVE_PRIVILEGE_FROM_ROLE_QUERY, roleId, removingPrivilegeId);
    }

    @Override
    protected Role insertCertainEntity(Role entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparePreparedStatementForInsert(ps, entity);
            return ps;
        }, keyHolder);


        return Role.builder()
                .withId(getIdOfSavedEntity(keyHolder))
                .withName(entity.getName())
                .build();
    }

    @Override
    protected void preparePreparedStatementForInsert(PreparedStatement ps, Role entity) throws SQLException {
        ps.setString(1, entity.getName());
    }

    @Override
    protected int updateCertainEntity(Role entity) {
        return jdbcTemplate.update(UPDATE_QUERY, entity.getName(), entity.getId());
    }

    @Override
    protected void preparePreparedStatementForUpdate(PreparedStatement ps, Role entity) throws SQLException {
        preparePreparedStatementForInsert(ps, entity);
        ps.setLong(2, entity.getId());
    }

}
