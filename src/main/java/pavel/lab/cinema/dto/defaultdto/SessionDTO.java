package pavel.lab.cinema.dto.defaultdto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "startTime", "movieTitle", "hallName", "price" })
public class SessionDTO {
    private Long id;
    private LocalDateTime startTime;
    private String movieTitle;
    private String hallName;
    private double price;
}
