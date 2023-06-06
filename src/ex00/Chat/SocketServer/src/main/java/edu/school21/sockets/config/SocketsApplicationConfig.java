package edu.school21.sockets.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import edu.school21.sockets.repositories.utils.TableInitializer;

@Configuration
@PropertySource("classpath:db.properties")
@ComponentScan("edu.school21")
public class SocketsApplicationConfig {

  @Value("${db.url}")
  private String url;

  @Value("${db.user}")
  private String user;

  @Value("${db.password}")
  private String password;

  @Value("${db.driver.name}")
  private String driverName;

  @Value("${db.path.schema}")
  private String pathSchema;

  @Value("${db.path.data}")
  private String pathData;

  @Bean
  public HikariConfig hikariConfig() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(url);
    hikariConfig.setUsername(user);
    hikariConfig.setPassword(password);
    hikariConfig.setDriverClassName(driverName);
    return hikariConfig;
  }

  @Bean
  public DataSource hikariDataSource() {
    return new HikariDataSource(hikariConfig());
  }

  @Bean
  public JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(hikariDataSource());
  }

  @Bean
  public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
    return new NamedParameterJdbcTemplate(hikariDataSource());
  }

  @Bean
  public PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder(16);
  }

  @Bean
  public TableInitializer tableInitializer() {
    TableInitializer tableInitializer = new TableInitializer(hikariDataSource());
    tableInitializer.setPathSchema(pathSchema);
    tableInitializer.setPathData(pathData);
    return tableInitializer;
  }

}
