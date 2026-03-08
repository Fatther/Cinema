package pavel.lab.cinema.dto.defaultdto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TicketDTO {
    private Long id;
    private Integer seatNumber;
    private SessionDTO session;
    private String visitorEmail;
}