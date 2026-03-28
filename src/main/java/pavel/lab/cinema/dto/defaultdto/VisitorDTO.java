package pavel.lab.cinema.dto.defaultdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitorDTO {
    private Long id;
    private String name;
    private String email;
}
