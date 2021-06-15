package ua.com.foxminded.university.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Department;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseDaoImpl extends AbstractPageableCrudDaoImpl<Course> implements CourseDao {

    private static final Logger LOGGER = Logger.getLogger(CourseDaoImpl.class);

    private static final String FIND_QUERY = "SELECT c.id, c.name, c.department_id, d.name as department_name " +
            "from courses c left join departments d on c.department_id = d.id ";
    private static final String FIND_BY_NAME_QUERY = FIND_QUERY + "WHERE c.name=?";
    private static final String SAVE_QUERY = "INSERT INTO courses (name) VALUES(?)";
    private static final String FIND_BY_ID_QUERY = FIND_QUERY + "WHERE c.id=?";
    private static final String FIND_ALL_NO_PAGES_QUERY = FIND_QUERY + "ORDER BY c.id";
    private static final String FIND_ALL_WITH_PAGES_QUERY = FIND_QUERY + "order by c.id offset ? row FETCH NEXT ? ROWS ONLY";
    private static final String UPDATE_QUERY = "UPDATE courses SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM courses WHERE id = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) as count from courses";
    private static final String FIND_BY_COURSE_ID = FIND_QUERY + "left join professor_course pc on c.id = pc.course_id " +
            "where pc.professor_id = ?";
    private static final String REMOVE_COURSE_FROM_PROFESSOR_COURSE_LIST_QUERY =
            "DELETE FROM professor_course WHERE professor_id = ? AND course_id = ?";
    private static final String ADD_COURSE_FROM_PROFESSOR_COURSE_LIST_QUERY =
            "INSERT INTO professor_course (professor_id, course_id) VALUES(?, ?)";
    private static final RowMapper<Course> ROW_MAPPER = (rs, rowNum) ->
         Course.builder()
                .withId(rs.getLong("id"))
                .withName(rs.getString("name"))
                .withDepartment(Department.builder()
                        .withId(rs.getLong("department_id"))
                        .withName(rs.getString("department_name"))
                        .build())
                .build();

    public CourseDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_NO_PAGES_QUERY, UPDATE_QUERY, DELETE_QUERY, ROW_MAPPER,
                FIND_ALL_WITH_PAGES_QUERY, COUNT_QUERY);
    }

    @Override
    public Optional<Course> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, ROW_MAPPER, name));
        } catch (DataAccessException e) {
            LOGGER.info("Department with this name not registered, Name: " + name);
            return Optional.empty();
        }
    }

    @Override
    public void addCourseToProfessorCourseList(long courseId, long professorId) {
        jdbcTemplate.update(ADD_COURSE_FROM_PROFESSOR_COURSE_LIST_QUERY, professorId, courseId);
    }

    @Override
    public void removeCourseFromProfessorCourseList(long courseId, long professorId) {
        jdbcTemplate.update(REMOVE_COURSE_FROM_PROFESSOR_COURSE_LIST_QUERY, professorId, courseId);
    }

    @Override
    public List<Course> findByProfessorId(long professorId){
        return jdbcTemplate.query(FIND_BY_COURSE_ID, ROW_MAPPER, professorId);
    }

    @Override
    protected Course insertCertainEntity(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparePreparedStatementForInsert(ps, course);
            return ps;
        }, keyHolder);

        return Course.builder()
                .withId(getIdOfSavedEntity(keyHolder))
                .withName(course.getName())
                .build();
    }

    @Override
    protected void preparePreparedStatementForInsert(PreparedStatement ps, Course course) throws SQLException {
        ps.setString(1, course.getName());
    }

    @Override
    protected int updateCertainEntity(Course course) {
        return jdbcTemplate.update(UPDATE_QUERY, course.getName(), course.getId());
    }

    @Override
    protected void preparePreparedStatementForUpdate(PreparedStatement ps, Course course) throws SQLException {
        preparePreparedStatementForInsert(ps, course);
        ps.setLong(2, course.getId());
    }

}
