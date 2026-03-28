package pavel.lab.cinema.dto.defaultdto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "title", "duration", "genres" })
public class MovieDTO {
    private Long id;
    private String title;
    private Integer duration;
    private List<String> genres;
}