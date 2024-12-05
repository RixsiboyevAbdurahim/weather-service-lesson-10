package entity.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class AuthUserDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public AuthUserDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void save(AuthUser authUser) {
        String sql = "insert into users (username, password, role) values (:username, :password, :role);";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("username", authUser.getUsername());
        parameterSource.addValue("password", authUser.getPassword());
        parameterSource.addValue("role", authUser.getRole());
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    public Optional<AuthUser> findByUsername(String username) {
        String sql = "select * from users where username = :username;";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("username", username);
        List<AuthUser> users = namedParameterJdbcTemplate.query(sql, parameterSource, new RowMapper<AuthUser>() {
            @Override
            public AuthUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                AuthUser user = AuthUser.builder()
                        .id(rs.getInt("id"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .role(rs.getString("role"))
                        .build();
                return user;
            }
        });
        if (users.isEmpty()) return Optional.empty();
        else return Optional.of(users.get(0));
    }
}
