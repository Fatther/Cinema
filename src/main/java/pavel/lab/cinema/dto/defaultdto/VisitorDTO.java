package pavel.lab.cinema.dto.defaultdto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VisitorDTO {
    private Long id;
    private String name;
    private String email;
}
