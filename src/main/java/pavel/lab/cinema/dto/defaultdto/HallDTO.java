package pavel.lab.cinema.dto.defaultdto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HallDTO {
    private Long id;
    private String name;
    private double price;
    private Integer seatAmount;
}