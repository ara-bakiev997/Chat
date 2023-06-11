package edu.school21.sockets.repositories.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
public class TableInitializer {
  private final JdbcTemplate jdbcTemplate;
  private String pathSchema;
  private String pathData;

  public void initializeTablesWithData() throws SQLException, IOException {

    String schemaQuery = Files.lines(Paths.get(pathSchema)).collect(Collectors.joining("\n"));
    String dataQuery = Files.lines(Paths.get(pathData)).collect(Collectors.joining("\n"));

    jdbcTemplate.update(schemaQuery);
    jdbcTemplate.update(dataQuery);
  }
}
