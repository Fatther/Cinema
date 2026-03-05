package pavel.lab.cinema.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionRequestDTO {
    private Long movieId;
    private LocalDateTime startTime;
    private Double price;
}
