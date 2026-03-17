package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pavel.lab.cinema.cache.CacheKey;
import pavel.lab.cinema.dto.defaultdto.MovieDTO;
import pavel.lab.cinema.dto.page.PageResponse;
import pavel.lab.cinema.dto.requestdto.MovieRequestDTO;
import pavel.lab.cinema.entity.Genre;
import pavel.lab.cinema.entity.Movie;
import pavel.lab.cinema.mapper.MovieMapper;
import pavel.lab.cinema.repository.GenreRepository;
import pavel.lab.cinema.repository.MovieRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final GenreRepository genreRepository;
    private final Map<CacheKey, PageResponse<MovieDTO>> movieCache = new HashMap<>();

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
        movieCache.clear();
        return movieMapper.toDto(savedMovie);
    }

    @Transactional
    public PageResponse<MovieDTO> findAll(Pageable pageable) {
        CacheKey key = new CacheKey(pageable);

        if (movieCache.containsKey(key)) {
            return movieCache.get(key);
        }

        Page<Movie> moviesPage = movieRepository.findAll(pageable);
        PageResponse<MovieDTO> dtosPage = new PageResponse<>(moviesPage.map(movieMapper::toDto));
        movieCache.put(key, dtosPage);
        return dtosPage;
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
        movieCache.clear();
        return movieMapper.toDto(savedMovie);
    }

    public void delete(Long id) {
        movieRepository.deleteById(id);
        movieCache.clear();
    }
}
