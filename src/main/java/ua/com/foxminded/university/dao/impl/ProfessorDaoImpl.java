package ua.com.foxminded.university.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.domain.Department;
import ua.com.foxminded.university.domain.Professor;
import ua.com.foxminded.university.domain.ScienceDegree;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProfessorDaoImpl extends AbstractPageableCrudDaoImpl<Professor> implements ProfessorDao {

    private static final String FIND_QUERY = "SELECT u.id, u.first_name, u.last_name, u.email, u.password, " +
            "u.department_id, d.name as department_name, u.sciencedegree_id " +
            "from users u left join departments d on u.department_id = d.id ";

    private static final String SAVE_QUERY = "INSERT INTO users (type, first_name, last_name, email, password, sciencedegree_id)" +
            " VALUES('professor', ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = FIND_QUERY + "WHERE u.id=? and type = 'professor'";
    private static final String FIND_ALL_NO_PAGES_QUERY = FIND_QUERY + "where type = 'professor' ORDER BY u.id";
    private static final String FIND_ALL_WITH_PAGES_QUERY = FIND_QUERY + "where type = 'professor' order by u.id offset ? row FETCH NEXT ? ROWS ONLY";
    private static final String UPDATE_QUERY = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ?, " +
            "sciencedegree_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) as count from users where type = 'professor'";
    private static final String FIND_BY_COURSE_ID = FIND_QUERY + "left join professor_course pc on u.id = pc.professor_id " +
        "where pc.course_id = ?";
    private static final String CHANGE_SCIENCE_DEGREE_QUERY = "UPDATE users SET sciencedegree_id = ? WHERE id = ?";

    private static final RowMapper<Professor> ROW_MAPPER = (rs, rowNum) ->
        Professor.builder()
                .withId(rs.getLong("id"))
                .withFirstName(rs.getString("first_name"))
                .withLastName(rs.getString("last_name"))
                .withEmail(rs.getString("email"))
                .withPassword(rs.getString("password"))
                .withDepartment(Department.builder()
                        .withId(rs.getLong("department_id"))
                        .withName(rs.getString("department_name"))
                        .build())
                .withScienceDegree(ScienceDegree.getById(rs.getInt("sciencedegree_id")))
                .build();

    public ProfessorDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_NO_PAGES_QUERY, UPDATE_QUERY, DELETE_QUERY, ROW_MAPPER,
                FIND_ALL_WITH_PAGES_QUERY, COUNT_QUERY);
    }

    @Override
    public void changeScienceDegree(long professorId, int idNewScienceDegree) {
        jdbcTemplate.update(CHANGE_SCIENCE_DEGREE_QUERY, idNewScienceDegree, professorId);
    }

    @Override
    public List<Professor> findByCourseId(long courseId) {
        return jdbcTemplate.query(FIND_BY_COURSE_ID, ROW_MAPPER, courseId);
    }

    @Override
    protected Professor insertCertainEntity(Professor professor) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparePSForInsert(ps, professor);
            return ps;
        }, keyHolder);
            return Professor.builder()
                    .withId(new Long(String.valueOf(keyHolder.getKeyList().get(0).get("id"))))
                    .withFirstName(professor.getFirstName())
                    .withLastName(professor.getLastName())
                    .withEmail(professor.getEmail())
                    .withPassword(professor.getPassword())
                    .withScienceDegree(professor.getScienceDegree())
                    .build();
    }

    @Override
    protected void preparePSForInsert(PreparedStatement ps, Professor professor) throws SQLException {
        ps.setString(1, professor.getFirstName());
        ps.setString(2, professor.getLastName());
        ps.setString(3, professor.getEmail());
        ps.setString(4, professor.getPassword());
        ps.setInt(5, professor.getScienceDegree().getId());
    }

    @Override
    protected int updateCertainEntity(Professor professor) {
        return jdbcTemplate.update(UPDATE_QUERY, professor.getFirstName(), professor.getLastName(), professor.getEmail(),
                        professor.getPassword(), professor.getScienceDegree().getId(), professor.getId());
    }

    @Override
    protected void preparePSForUpdate(PreparedStatement ps, Professor professor) throws SQLException {
        preparePSForInsert(ps, professor);
        ps.setLong(6, professor.getId());
    }

}
