package ua.com.foxminded.university.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Professor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class LessonDaoImpl extends AbstractPageableCrudDaoImpl<Lesson> implements LessonDao {

    private static final String FIND_QUERY = "SELECT l.id, l.course_id, c.name as course_name, timeOfStart as timeOfStart, " +
            "l.group_id, g.name as group_name, l.professor_id, u.first_name as professor_firstname, " +
            "u.last_name as professor_lastname, l.formoflesson_id, f.name as formoflesson_name " +
            "from lessons l left join courses c on l.course_id  = c.id left join groups g on l.group_id = g.id " +
            "left join users u on l.professor_id = u.id left join formsoflesson f on l.formoflesson_id = f.id ";
    private static final String SAVE_QUERY = "INSERT INTO lessons (timeOfStart) VALUES(?)";
    private static final String FIND_BY_ID_QUERY = FIND_QUERY + "WHERE l.id=?";
    private static final String FIND_ALL_NO_PAGES_QUERY = FIND_QUERY + "ORDER BY l.id";
    private static final String FIND_ALL_WITH_PAGES_QUERY = FIND_QUERY + "order by l.id offset ? row FETCH NEXT ? ROWS ONLY";
    private static final String UPDATE_QUERY = "UPDATE lessons SET timeOfStart = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM lessons WHERE id = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) as count from lessons";
    private static final String CHANGE_FORM_OF_LESSON_QUERY = "UPDATE lessons SET formoflesson_id = ? WHERE id = ?";
    private static final String CHANGE_TEACHER_QUERY = "UPDATE lessons SET professor_id = ? WHERE id = ?";
    private static final String CHANGE_COURSE_QUERY = "UPDATE lessons SET course_id = ? WHERE id = ?";
    private static final String FIND_BY_GROUP_ID = FIND_QUERY + "WHERE l.group_id=? ORDER BY timeOfStart";
    private static final String FIND_BY_PROFESSOR_ID = FIND_QUERY + "WHERE l.professor_id=? ORDER BY timeOfStart";
    private static final RowMapper<Lesson> ROW_MAPPER = (rs, rowNum) ->
        Lesson.builder()
                .withId(rs.getLong("id"))
                .withCourse(Course.builder()
                        .withId(rs.getLong("course_id"))
                        .withName(rs.getString("course_name"))
                        .build())
                .withTimeOfStartLesson(nullSafeDateExtract(rs, "timeOfStart"))
                .withGroup(Group.builder()
                        .withId(rs.getLong("group_id"))
                        .withName(rs.getString("group_name"))
                        .build())
                .withTeacher(Professor.builder()
                        .withId(rs.getLong("professor_id"))
                        .withFirstName(rs.getString("professor_firstname"))
                        .withLastName(rs.getString("professor_lastname"))
                        .build())
                .withFormOfLesson(FormOfLesson.builder()
                        .withId(rs.getLong("formoflesson_id"))
                        .withName(rs.getString("formoflesson_name"))
                        .build())
                .build();

    public LessonDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_NO_PAGES_QUERY, UPDATE_QUERY, DELETE_QUERY, ROW_MAPPER,
                FIND_ALL_WITH_PAGES_QUERY, COUNT_QUERY);
    }

    @Override
    public List<Lesson> formTimeTableForGroup(long groupId) {
        return jdbcTemplate.query(FIND_BY_GROUP_ID, ROW_MAPPER, groupId);
    }

    @Override
    public List<Lesson> formTimeTableForProfessor(long professorId) {
        return jdbcTemplate.query(FIND_BY_PROFESSOR_ID, ROW_MAPPER, professorId);
    }

    @Override
    public void changeFormOfLesson(long lessonId, long newFormOfLessonId) {
        jdbcTemplate.update(CHANGE_FORM_OF_LESSON_QUERY, newFormOfLessonId, lessonId);
    }

    @Override
    public void changeTeacher(long lessonId, long newProfessorId) {
        jdbcTemplate.update(CHANGE_TEACHER_QUERY, newProfessorId, lessonId);
    }

    @Override
    public void changeCourse(long lessonId, long newCourseId) {
        jdbcTemplate.update(CHANGE_COURSE_QUERY, newCourseId, lessonId);
    }

    @Override
    protected Lesson insertCertainEntity(Lesson lesson) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparePreparedStatementForInsert(ps, lesson);
            return ps;
        }, keyHolder);

        return Lesson.builder()
                    .withId(getIdOfSavedEntity(keyHolder))
                    .withTimeOfStartLesson(lesson.getTimeOfStartLesson())
                    .build();
    }

    @Override
    protected void preparePreparedStatementForInsert(PreparedStatement ps, Lesson lesson) throws SQLException {
        if (lesson.getTimeOfStartLesson() == null) {
            ps.setTimestamp(1, null);
        } else {
            ps.setTimestamp(1, Timestamp.valueOf(lesson.getTimeOfStartLesson()));
        }

    }

    @Override
    protected int updateCertainEntity(Lesson lesson) {
        return jdbcTemplate.update(UPDATE_QUERY, lesson.getTimeOfStartLesson(), lesson.getId());
    }

    @Override
    protected void preparePreparedStatementForUpdate(PreparedStatement ps, Lesson lesson) throws SQLException {
        preparePreparedStatementForInsert(ps, lesson);
        ps.setLong(2, lesson.getId());
    }

    private static LocalDateTime nullSafeDateExtract(ResultSet rs, String columnName){
        LocalDateTime localDateTime;
        try{
            localDateTime = rs.getTimestamp(columnName).toLocalDateTime();
            return localDateTime;
        } catch (Exception exception) {
            return null;
        }
    }

}
