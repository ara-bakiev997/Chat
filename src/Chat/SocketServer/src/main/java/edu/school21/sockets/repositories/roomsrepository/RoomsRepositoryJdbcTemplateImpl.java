package edu.school21.sockets.repositories.roomsrepository;

import edu.school21.sockets.models.ChatRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Component
public class RoomsRepositoryJdbcTemplateImpl implements RoomsRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public RoomsRepositoryJdbcTemplateImpl(
      @Qualifier("roomsRepositoryNamedParameter") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public Optional<ChatRoom> findById(Long id) {
    String queryForFindById = "select * from chat_rooms where id = :id;";

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("id", id);

    return namedParameterJdbcTemplate.query(queryForFindById, params, (rs) -> {
      if (rs.next()) {
        return Optional.of(new ChatRoom(rs.getLong("id"), rs.getString("name"), null));
      }
      return Optional.empty();
    });
  }

  @Override
  public List<ChatRoom> findAll() {
    return namedParameterJdbcTemplate.query("select * from chat_rooms order by id",
        (rs, rowNum) -> {
          return new ChatRoom(rs.getLong("id"), rs.getString("name"), null);
        });
  }

  @Override
  public void save(ChatRoom entity) {
    String queryForSave = "insert into chat_rooms(name, creator) values (:name, :creator)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("name", entity.getName());
    params.addValue("creator", entity.getCreator().getId());

    namedParameterJdbcTemplate.update(queryForSave, params, keyHolder);

    entity.setId((Long) keyHolder.getKeys().get("id"));
  }

  @Override
  public void update(ChatRoom entity) {
    String queryForUpdate =
        "update chat_rooms set name = :name, creator = :creator where id = :id;";

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("name", entity.getName());
    params.addValue("creator", entity.getCreator().getId());
    params.addValue("id", entity.getId());

    if (namedParameterJdbcTemplate.update(queryForUpdate, params) == 0) {
      throw new IllegalArgumentException("Failed to update entity");
    }
  }

  @Override
  public void delete(Long id) {
    String queryForDelete = "delete from chat_rooms where id = :id;";

    if (namedParameterJdbcTemplate.update(queryForDelete,
        new MapSqlParameterSource().addValue("id", id)) == 0) {
      throw new IllegalArgumentException("Failed to delete entity");
    }
  }

  @Override
  public Optional<ChatRoom> findByName(String name) {
    String queryForFindByName = "select * from chat_rooms where name = :name;";
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("id", name);

    return namedParameterJdbcTemplate.query(queryForFindByName, params, (rs) -> {
      if (rs.next()) {
        return Optional.of(new ChatRoom(rs.getLong("id"), rs.getString("name"), null));
      }
      return Optional.empty();
    });
  }

}
