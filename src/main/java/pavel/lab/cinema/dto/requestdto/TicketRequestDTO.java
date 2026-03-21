package pavel.lab.cinema.dto.requestdto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDTO {

    @NotNull
    private Long sessionId;

    @NotNull
    private Long visitorId;

    @NotNull
    @Positive
    private Integer seatNumber;
}
