package edu.school21.sockets.repositories.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class TableInitializer {
  private final JdbcTemplate jdbcTemplate;
  private Path pathSchema;
  private Path pathData;

  public TableInitializer(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public void setPathSchema(String pathSchema) {
    this.pathSchema = Paths.get(pathSchema);
  }

  public void setPathData(String pathData) {
    this.pathData = Paths.get(pathData);
  }

  public void initializeTablesWithData() throws SQLException, IOException {

    String schemaQuery = Files.lines(pathSchema).collect(Collectors.joining("\n"));
    String dataQuery = Files.lines(pathData).collect(Collectors.joining("\n"));

    jdbcTemplate.update(schemaQuery);
    jdbcTemplate.update(dataQuery);
  }
}
