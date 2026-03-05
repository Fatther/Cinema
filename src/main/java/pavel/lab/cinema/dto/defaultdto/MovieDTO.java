package pavel.lab.cinema.dto.defaultdto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonPropertyOrder({ "id", "title", "duration", "genres" })
public class MovieDTO {
    private Long id;
    private String title;
    private Integer duration;
    private List<String> genres;
}