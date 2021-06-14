package ua.com.foxminded.university.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.domain.Group;
import ua.com.foxminded.university.domain.Student;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class StudentDaoImpl extends AbstractPageableCrudDaoImpl<Student> implements StudentDao {

    private static final String FIND_QUERY = "SELECT u.id, u.first_name, u.last_name, u.email, u.password, u.group_id, " +
            "g.name as group_name from users u left join groups g on u.group_id = g.id ";
    private static final String SAVE_QUERY = "INSERT INTO users (type, first_name, last_name, email, password)" +
            " VALUES('student', ?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = FIND_QUERY + "WHERE u.id=? and type = 'student'";
    private static final String FIND_ALL_NO_PAGES_QUERY = FIND_QUERY + "where type = 'student' ORDER BY u.id";
    private static final String FIND_ALL_WITH_PAGES_QUERY = FIND_QUERY + "where type = 'student' order by u.id offset ? row FETCH NEXT ? ROWS ONLY";
    private static final String UPDATE_QUERY = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) as count from users where type = 'student'";
    private static final String CHANGE_GROUP_QUERY = "UPDATE users SET group_id = ? WHERE id = ?";
    private static final String LEAVE_GROUP_QUERY = "UPDATE users SET group_id = null WHERE id = ?";
    private static final RowMapper<Student> ROW_MAPPER = (rs, rowNum) ->
        Student.builder()
                .withId(rs.getLong("id"))
                .withFirstName(rs.getString("first_name"))
                .withLastName(rs.getString("last_name"))
                .withEmail(rs.getString("email"))
                .withPassword(rs.getString("password"))
                .withGroup(
                        Group.builder()
                        .withId(rs.getLong("group_id"))
                        .withName(rs.getString("group_name"))
                        .build())
                .build();

    public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_NO_PAGES_QUERY, UPDATE_QUERY, DELETE_QUERY, ROW_MAPPER,
                FIND_ALL_WITH_PAGES_QUERY, COUNT_QUERY);
    }

    @Override
    public void leaveGroup(long studentId) {
        jdbcTemplate.update(LEAVE_GROUP_QUERY, studentId);
    }

    @Override
    public void enterGroup(long studentId, long groupId) {
        jdbcTemplate.update(CHANGE_GROUP_QUERY, groupId, studentId);
    }

    @Override
    protected Student insertCertainEntity(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparePSForInsert(ps, student);
            return ps;
        }, keyHolder);

        return Student.builder()
                    .withId(new Long(String.valueOf(keyHolder.getKeyList().get(0).get("id"))))
                    .withFirstName(student.getFirstName())
                    .withLastName(student.getLastName())
                    .withEmail(student.getEmail())
                    .withPassword(student.getPassword())
                    .build();
    }

    @Override
    protected void preparePSForInsert(PreparedStatement ps, Student student) throws SQLException {
        ps.setString(1, student.getFirstName());
        ps.setString(2, student.getLastName());
        ps.setString(3, student.getEmail());
        ps.setString(4, student.getPassword());
    }

    @Override
    protected int updateCertainEntity(Student student) {
        return jdbcTemplate.update(UPDATE_QUERY, student.getFirstName(), student.getLastName(),
                student.getEmail(), student.getPassword(), student.getId());
    }

    @Override
    protected void preparePSForUpdate(PreparedStatement ps, Student student) throws SQLException {
        preparePSForInsert(ps, student);
        ps.setLong(5, student.getId());
    }

}
