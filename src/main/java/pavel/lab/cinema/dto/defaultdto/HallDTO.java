package pavel.lab.cinema.dto.defaultdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HallDTO {
    private Long id;
    private String name;
    private double price;
    private Integer seatAmount;
}