package entity.models.weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Weather {
    private int id;
    private int city_id;
    private LocalDate date;
    private float temperature;
    private String description;
}
