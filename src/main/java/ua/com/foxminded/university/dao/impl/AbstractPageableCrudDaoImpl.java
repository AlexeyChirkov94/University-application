package ua.com.foxminded.university.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import ua.com.foxminded.university.dao.interfaces.CrudPageableDao;
import java.util.List;

public abstract class AbstractPageableCrudDaoImpl<E> extends AbstractCrudDaoImpl<E> implements CrudPageableDao<E> {

    private final String findAllWithPagesQuery;
    private final String countQuery;

    protected AbstractPageableCrudDaoImpl(JdbcTemplate jdbcTemplate, String saveQuery, String findByIdQuery,
                                          String findAllNoPagesQuery, String updateQuery, String deleteQuery,
                                          RowMapper<E> rowMapper, String findAllWithPagesQuery, String countQuery) {
        super(jdbcTemplate, saveQuery, findByIdQuery, findAllNoPagesQuery, updateQuery, deleteQuery, rowMapper);
        this.findAllWithPagesQuery = findAllWithPagesQuery;
        this.countQuery = countQuery;
    }

    public List<E> findAll(int page, int itemsPerPage){
        int offset = itemsPerPage * (page - 1);
        PreparedStatementSetter preparedStatementSetter = (ps) -> {
            ps.setInt(1, offset);
            ps.setInt(2, itemsPerPage);
        };

        return jdbcTemplate.query(findAllWithPagesQuery, preparedStatementSetter, rowMapper);
    }

    @Override
    public long count(){
        return jdbcTemplate.queryForObject(countQuery, Long.class, new Object[] {});
    }

}
