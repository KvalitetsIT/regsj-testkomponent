package dk.kvalitetsit.regsj.testkomponent.dao;

import dk.kvalitetsit.regsj.testkomponent.dao.entity.LastAccessed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Optional;

public class LastAccessedDaoImpl implements LastAccessedDao {
    private static final Logger logger = LoggerFactory.getLogger(LastAccessedDaoImpl.class);
    private final DataSource dataSource;

    public LastAccessedDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(LastAccessed helloEntity) {
        logger.info("Inserting new entry in database.");

        var sql = "insert into last_accessed(access_time) values(:access_time)";
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);

        var parameterMap = new HashMap<String, Object>();
        parameterMap.put("access_time", helloEntity.getAccessTime());

        template.update(sql, parameterMap);
    }

    @Override
    public Optional<LastAccessed> getLatest() {
        var sql = "select * from last_accessed order by id desc limit 1";

        var template = new NamedParameterJdbcTemplate(dataSource);
        var result = template.query(sql, BeanPropertyRowMapper.newInstance(LastAccessed.class));

        return result.stream().findFirst();
    }
}
