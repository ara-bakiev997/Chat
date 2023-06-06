package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Component
public class UsersRepositoryJdbcTemplateImpl implements UsersRepository {
  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public UsersRepositoryJdbcTemplateImpl(JdbcTemplate jdbcTemplate,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public Optional<User> findById(Long id) {
    String queryForFindById = "select * from user_table where id = :id;";

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("id", id);

    return namedParameterJdbcTemplate.query(queryForFindById, params, (rs) -> {
      if (rs.next()) {
        return Optional
            .of(new User(rs.getLong("id"), rs.getString("name"), rs.getString("password")));
      }
      return Optional.empty();
    });
  }

  @Override
  public List<User> findAll() {
    return jdbcTemplate.query("select * from user_table order by id",
        new BeanPropertyRowMapper<>(User.class));
  }

  @Override
  public void save(User entity) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(con -> {
      PreparedStatement statement = con.prepareStatement(
          "insert into user_table(name, password) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, entity.getUserName());
      statement.setString(2, entity.getPassword());
      return statement;
    }, keyHolder);

    entity.setId((Long) keyHolder.getKeys().get("id"));
  }

  @Override
  public void update(User entity) {
    String queryForUpdate =
        "update user_table set name = :name, password = :password where id = :id;";

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("name", entity.getUserName());
    params.addValue("password", entity.getPassword());
    params.addValue("id", entity.getId());

    if (namedParameterJdbcTemplate.update(queryForUpdate, params) == 0) {
      throw new IllegalArgumentException("Failed to update entity");
    }
  }

  @Override
  public void delete(Long id) {
    String queryForDelete = "delete from user_table where id = :id;";

    if (namedParameterJdbcTemplate.update(queryForDelete,
        new MapSqlParameterSource().addValue("id", id)) == 0) {
      throw new IllegalArgumentException("Failed to delete entity");
    }
  }

  @Override
  public Optional<User> findByName(String name) {
    return jdbcTemplate.query("select * from user_table where name = ?;", (ps) -> {
      ps.setString(1, name);
    }, (rs) -> {
      if (rs.next()) {
        return Optional
            .of(new User(rs.getLong("id"), rs.getString("name"), rs.getString("password")));
      }
      return Optional.empty();
    });
  }

}
