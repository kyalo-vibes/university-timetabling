package com.kyalo.universitytimetabling.controller;

import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/reset")
@PreAuthorize("hasAuthority('ADMIN')")
public class ResetController {

    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;

    @Autowired
    public ResetController(DataSource dataSource, ResourceLoader resourceLoader) {
        this.dataSource = dataSource;
        this.resourceLoader = resourceLoader;
    }

    @PostMapping
    public String resetDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            dropTables(connection);
            createAndPopulateTables(connection);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return "Database reset failed.";
        }
        return "Database reset completed successfully.";
    }

    private void dropTables(Connection connection) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");
        String getTablesSql = "SHOW TABLES";
        List<String> tables = jdbcTemplate.queryForList(getTablesSql, String.class);
        for (String table : tables) {
            jdbcTemplate.execute("DROP TABLE IF EXISTS " + table);
        }
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1;");
    }


    private void createAndPopulateTables(Connection connection) throws SQLException, IOException {
        executeSqlScript(connection, "classpath:database_dump.sql");
//        executeSqlScript(connection, "classpath:insert.sql");
    }

    private void executeSqlScript(Connection connection, String scriptLocation) throws SQLException, IOException {
        Resource resource = resourceLoader.getResource(scriptLocation);
        ScriptUtils.executeSqlScript(connection, resource);
    }

}

