package entity.models.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {
    private int id;
    private int user_id;
    private int city_id;
    private LocalDate subscription_date;
}
