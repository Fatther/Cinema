package pavel.lab.cinema.dto.defaultdto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SessionDTO {
    private Long id;
    private LocalDateTime startTime;
    private Double price;
    private String movie;
}
