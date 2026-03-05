package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pavel.lab.cinema.dto.defaultdto.MovieDTO;
import pavel.lab.cinema.dto.requestdto.MovieRequestDTO;
import pavel.lab.cinema.entity.Genre;
import pavel.lab.cinema.entity.Movie;
import pavel.lab.cinema.mapper.MovieMapper;
import pavel.lab.cinema.repository.GenreRepository;
import pavel.lab.cinema.repository.MovieRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final GenreRepository genreRepository;

    @Transactional
    public MovieDTO create(
            MovieRequestDTO dto
    ) {
        Movie movie = movieMapper.toEntity(dto);
        List<Genre> genres = genreRepository.findAllById(dto.getGenreIds());
        if (genres.size() != dto.getGenreIds().size()) {
            throw new EntityNotFoundException("Ошибка в списке жанров");
        }
        movie.setGenres(genres);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    @Transactional
    public List<MovieDTO> findAll() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(movieMapper::toDto)
                .toList();
    }

    @Transactional
    public MovieDTO findById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сущность с ID " + id + " не найдена"));
        return movieMapper.toDto(movie);
    }

    @Transactional
    public MovieDTO update(Long id, MovieRequestDTO dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сущность с ID " + id + " не найдена"));
        movie.setTitle(dto.getTitle());
        movie.setDuration(dto.getDuration());
        List<Genre> genres = genreRepository.findAllById(dto.getGenreIds());
        if (genres.size() != dto.getGenreIds().size()) {
            throw new EntityNotFoundException("Ошибка в списке жанров");
        }
        movie.setGenres(genres);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    public void delete(Long id) {
        movieRepository.deleteById(id);
    }
}
