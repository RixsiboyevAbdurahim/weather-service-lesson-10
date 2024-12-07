package entity.models.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WeatherDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void save(Weather weather) {
        String sql = "insert into weather_data (city_id, date, temperature, description) values (:city_id, :date, :temperature, :description);";
        var paramSource = new MapSqlParameterSource();
        paramSource.addValue("city_id", weather.getCity_id());
        paramSource.addValue("date", weather.getDate());
        paramSource.addValue("temperature", weather.getTemperature());
        paramSource.addValue("description", weather.getDescription());
        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    public List<Weather> findAllByCityId(Integer cityId) {
        String sql = "select * from weather_data where city_id = :cityId";
        var paramSource = new MapSqlParameterSource("cityId", cityId);
        return namedParameterJdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper<>(Weather.class));
    }
}
