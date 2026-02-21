package pavel.lab.cinema.mapper;

import org.springframework.stereotype.Component;
import pavel.lab.cinema.dto.MovieDto;
import pavel.lab.cinema.entities.Movie;

@Component
public class MovieMapper {
    public MovieDto movieToDto(Movie movie) {
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setDuration(movie.getDuration());
        movieDto.setAgeRating(movie.getAgeRating());
        movieDto.setGenre(movie.getGenre());
        return movieDto;
    }
}
