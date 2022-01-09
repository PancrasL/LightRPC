package github.pancras.service2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DemoDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(String name) {
        jdbcTemplate.update("insert into test (name) values(?)", name);
    }
}
