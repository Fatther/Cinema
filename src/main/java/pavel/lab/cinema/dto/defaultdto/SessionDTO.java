package pavel.lab.cinema.dto.defaultdto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonPropertyOrder({ "id", "startTime", "movieTitle", "hallName", "price" })
public class SessionDTO {
    private Long id;
    private LocalDateTime startTime;
    private String movieTitle;
    private String hallName;
    private double price;
}
