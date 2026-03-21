package pavel.lab.cinema.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitorRequestDTO {

    @NotBlank
    @Size(min = 1, max = 20)
    private String name;

    @NotBlank
    @Email
    @Size(min = 11, max = 30)
    private String email;
}
