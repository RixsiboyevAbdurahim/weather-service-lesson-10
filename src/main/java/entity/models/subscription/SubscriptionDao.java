package entity.models.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void save(Subscription subscription) {
        String sql = "insert into subscription (user_id, city_id, subscription_date) values (:user_id, :city_id, :subscription_date);";
        var paramSource = new MapSqlParameterSource();
        paramSource.addValue("user_id", subscription.getUser_id());
        paramSource.addValue("city_id", subscription.getCity_id());
        paramSource.addValue("subscription_date", subscription.getSubscription_date());
        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    public List<Subscription> findAll() {
        String sql = "select * from subscription;";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Subscription.class));
    }
}
