package entity.models.inner;

import entity.models.CustomUser;
import entity.models.weather.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InnerJoinDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Weather> getAllWeatherByUserId(int userId) {
        String sql = "select wd.* from subscription sub inner join main.weather_data wd on sub.city_id = wd.city_id where user_id = :id order by date desc";
        return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource().addValue("id", userId), new BeanPropertyRowMapper<>(Weather.class));
    }

    public List<CustomUser> getByRoleUser() {
        String sql = "select * from users u inner join subscription s on u.id = s.user_id inner join city c on c.id = s.city_id where role = 'USER';";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CustomUser.class));
    }

}
