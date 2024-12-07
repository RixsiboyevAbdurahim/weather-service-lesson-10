package entity.models.city;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CityDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void save(String cityName) {
        String sql = "insert into city (name) values (:name);";
        var paramSource = new MapSqlParameterSource("name", cityName);
        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    public List<City> findAll() {
        String sql = "select * from city";
        return namedParameterJdbcTemplate.query(sql, new RowMapper<City>() {

            @Override
            public City mapRow(ResultSet rs, int rowNum) throws SQLException {
                return City.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .build();
            }
        });
    }

    public City findById(int id) {
        String sql = "select * from city where id = :id";
        List<City> cities = namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource("id", id), new RowMapper<City>() {
            @Override
            public City mapRow(ResultSet rs, int rowNum) throws SQLException {
                return City.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .build();
            }
        });
        return cities.isEmpty() ? null : cities.get(0);
    }

    public void update(@NonNull City city) {
        String sql = "update city set name=:name where id=:id";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource("name", city.getName()));
    }

    public void delete(int id) {
        String sql = "delete from city where id = :id";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
    }

}
