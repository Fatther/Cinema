package pavel.lab.cinema.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HallRequestDTO {

    @NotBlank
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    @Positive
    private double price;

    @NotNull
    @Positive
    private Integer seatAmount;
}