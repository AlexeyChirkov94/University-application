package ua.com.foxminded.university.dao.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.entity.Department;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Repository
@Log4j
public class DepartmentDaoImpl extends AbstractPageableCrudDaoImpl<Department> implements DepartmentDao {

    private static final String SAVE_QUERY = "INSERT INTO departments (name) VALUES(?)";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name from departments WHERE id=?";
    private static final String FIND_BY_NAME_QUERY = "SELECT id, name from departments WHERE name=?";
    private static final String FIND_ALL_NO_PAGES_QUERY = "SELECT id, name FROM departments ORDER BY id";
    private static final String FIND_ALL_WITH_PAGES_QUERY = "SELECT * FROM departments order by id offset ? row FETCH NEXT ? ROWS ONLY";
    private static final String UPDATE_QUERY = "UPDATE departments SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM departments WHERE id = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) as count from departments";
    private static final RowMapper<Department> ROW_MAPPER = (rs, rowNum) ->
        Department.builder()
                .withId(rs.getLong("id"))
                .withName(rs.getString("name"))
                .build();

    public DepartmentDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_NO_PAGES_QUERY, UPDATE_QUERY, DELETE_QUERY, ROW_MAPPER,
                FIND_ALL_WITH_PAGES_QUERY, COUNT_QUERY);
    }

    @Override
    public Optional<Department> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, ROW_MAPPER, name));
        } catch (DataAccessException e) {
            log.info("Department with this name not registered, Name: " + name);
            return Optional.empty();
        }
    }
    
    @Override
    protected Department insertCertainEntity(Department department){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparePreparedStatementForInsert(ps, department);
            return ps;
            }, keyHolder);


        return Department.builder()
                .withId(getIdOfSavedEntity(keyHolder))
                .withName(department.getName())
                .build();
    }

    @Override
    protected void preparePreparedStatementForInsert(PreparedStatement ps, Department department) throws SQLException {
        ps.setString(1, department.getName());
    }

    @Override
    protected int updateCertainEntity(Department department) {
        return jdbcTemplate.update(UPDATE_QUERY, department.getName(), department.getId());
    }

    @Override
    protected void preparePreparedStatementForUpdate(PreparedStatement ps, Department department) throws SQLException {
        preparePreparedStatementForInsert(ps, department);
        ps.setLong(2, department.getId());
    }

}
