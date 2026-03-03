package pavel.lab.cinema.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pavel.lab.cinema.dto.MovieDto;
import pavel.lab.cinema.entities.Movie;
import pavel.lab.cinema.mapper.MovieMapper;
import pavel.lab.cinema.repository.MovieRepository;

@Service
@RequiredArgsConstructor
public class CinemaService {

    private final MovieRepository cinemaRepository;
    private final MovieMapper movieMapper;

    public List<MovieDto> findAllMovies() {
        List<MovieDto> movieDtoList = new ArrayList<>();
        for (Movie movie : cinemaRepository.findAllMovies()) {
            movieDtoList.add(movieMapper.movieToDto(movie));
        }
        return movieDtoList;
    }

    public List<MovieDto> findMoviesByDuration(Integer maxDuration) {
        List<MovieDto> movieDurationDtoList = new ArrayList<>();
        for (MovieDto movieDto : this.findAllMovies()) {
            if (movieDto.getDuration() <= maxDuration) {
                movieDurationDtoList.add(movieDto);
            }
        }
        return movieDurationDtoList;
    }

    public MovieDto findMovieById(Long id) {
        if (cinemaRepository.findMovieById(id) == null) {
            return null;
        }
        return movieMapper.movieToDto(cinemaRepository.findMovieById(id));
    }
}
