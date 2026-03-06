package pavel.lab.cinema.dto.requestdto;

import lombok.Data;

@Data
public class HallRequestDTO {
    private String name;
    private double price;
    private Integer seatAmount;
}