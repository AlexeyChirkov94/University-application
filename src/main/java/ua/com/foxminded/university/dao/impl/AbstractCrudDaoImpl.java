package ua.com.foxminded.university.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import ua.com.foxminded.university.dao.interfaces.CrudDao;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E> {

    private static final Logger LOGGER = Logger.getLogger(AbstractCrudDaoImpl.class);

    private static final String MESSAGE_OF_SUCCESS_DELETING = "Entity deleted, id = ";
    private static final String MESSAGE_OF_ERROR_IN_DELETING = "Error entity deleting, id = ";

    protected JdbcTemplate jdbcTemplate;
    private final String saveQuery;
    private final String findByIdQuery;
    private final String findAllNoPagesQuery;
    private final String updateQuery;
    private final String deleteQuery;
    protected final RowMapper<E> rowMapper;

    protected AbstractCrudDaoImpl(JdbcTemplate jdbcTemplate, String saveQuery, String findByIdQuery,
                                  String findAllNoPagesQuery, String updateQuery, String deleteQuery, RowMapper<E> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.saveQuery = saveQuery;
        this.findByIdQuery = findByIdQuery;
        this.findAllNoPagesQuery = findAllNoPagesQuery;
        this.updateQuery = updateQuery;
        this.deleteQuery = deleteQuery;
        this.rowMapper = rowMapper;
    }

    @Override
    public E save(E entity) {
        return insertCertainEntity(entity);
    }

    @Override
    public void saveAll(List<E> entities){
        jdbcTemplate.batchUpdate(saveQuery,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int numberOfElement)
                            throws SQLException {
                        E entity = entities.get(numberOfElement);
                        preparePreparedStatementForInsert(preparedStatement, entity);
                    }
                    @Override
                    public int getBatchSize() {
                        return entities.size();
                    }
                });
    }

    @Override
    public Optional<E> findById(Long id){
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(findByIdQuery, rowMapper, id));
        } catch (DataAccessException e) {
            LOGGER.info("Entity not found: " + id);
            return Optional.empty();
        }
    }

    @Override
    public List<E> findAll(){
        return jdbcTemplate.query(findAllNoPagesQuery, rowMapper);
    }

    @Override
    public void update(E entity) {
        updateCertainEntity(entity);
    }

    @Override
    public void updateAll(List<E> entities) {
        jdbcTemplate.batchUpdate(updateQuery,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int numberOfElement)
                            throws SQLException {
                        E entity = entities.get(numberOfElement);
                        preparePreparedStatementForUpdate(preparedStatement, entity);
                    }
                    @Override
                    public int getBatchSize() {
                        return entities.size();
                    }
                });
    }

    @Override
    public boolean deleteById(Long id) {
        int result = jdbcTemplate.update(deleteQuery, id);
        if (result == 1){
            LOGGER.info(MESSAGE_OF_SUCCESS_DELETING + id);
            return true;
        } else {
            LOGGER.info(MESSAGE_OF_ERROR_IN_DELETING + id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Long> ids) {
        List<Long> listIds = new ArrayList<>(ids);
        boolean status = true;
        int [] result = jdbcTemplate.batchUpdate(deleteQuery,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int numberOfElement)
                            throws SQLException {
                        Long id = listIds.get(numberOfElement);
                        preparedStatement.setLong(1, id);
                    }
                    @Override
                    public int getBatchSize() {
                        return ids.size();
                    }
                });
        if(Arrays.stream(result).anyMatch(a -> a == 0)){
            status = false;
        }

        return status;
    }

    protected Long getIdOfSavedEntity(KeyHolder keyHolder){
        return Long.valueOf(String.valueOf(keyHolder.getKeyList().get(0).get("id")));
    }

    protected abstract E insertCertainEntity(E entity);

    protected abstract void preparePreparedStatementForInsert(PreparedStatement ps, E entity) throws SQLException;

    protected abstract int updateCertainEntity(E entity);

    protected abstract void preparePreparedStatementForUpdate(PreparedStatement ps, E entity) throws SQLException;

}
