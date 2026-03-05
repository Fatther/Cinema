package pavel.lab.cinema.mapper;

import org.springframework.stereotype.Component;
import pavel.lab.cinema.dto.defaultdto.MovieDTO;
import pavel.lab.cinema.dto.requestdto.MovieRequestDTO;
import pavel.lab.cinema.entity.Genre;
import pavel.lab.cinema.entity.Movie;

@Component
public class MovieMapper {

    public MovieDTO toDto(Movie entity) {
        if (entity == null) {
            return null;
        }

        return MovieDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .duration(entity.getDuration())
                .genres(entity.getGenres() != null
                        ? entity.getGenres().stream()
                                .map(Genre::getName)
                                .toList() : null)
                .build();
    }

    public Movie toEntity(MovieRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Movie.builder()
                .title(dto.getTitle())
                .duration(dto.getDuration())
                .build();
    }
}