package pavel.lab.cinema.dto.defaultdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    private Integer seatNumber;
    private SessionDTO session;
    private String visitorEmail;
}