package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pavel.lab.cinema.dto.defaultdto.GenreDTO;
import pavel.lab.cinema.dto.requestdto.GenreRequestDTO;
import pavel.lab.cinema.entity.Genre;
import pavel.lab.cinema.entity.Movie;
import pavel.lab.cinema.mapper.GenreMapper;
import pavel.lab.cinema.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    private static final String NOT_FOUND_MSG = " not found";
    private static final String GENRE_PREFIX = "Genre with ID ";

    @Transactional
    public GenreDTO create(
            GenreRequestDTO dto
    ) {
        Genre genre = genreMapper.toEntity(dto);
        Genre savedGenre = genreRepository.save(genre);
        return genreMapper.toDto(savedGenre);
    }

    @Transactional(readOnly = true)
    public List<GenreDTO> findAll() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream()
                .map(genreMapper::toDto)
                .toList();
    }

    @Transactional
    public GenreDTO findById(
            Long id
    ) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(GENRE_PREFIX + id + NOT_FOUND_MSG));
        return genreMapper.toDto(genre);
    }

        @Transactional
        public void delete(Long id) {
            Genre genre = genreRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(GENRE_PREFIX + id + NOT_FOUND_MSG));

            List<Movie> movies = genre.getMovies();
            if (!movies.isEmpty()) {
                for (Movie movie : movies) {
                    movie.getGenres().remove(genre);
                }
            }
            genreRepository.delete(genre);
        }

    @Transactional
    public GenreDTO update(
            Long id,
            GenreRequestDTO dto
    ) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(GENRE_PREFIX + id + NOT_FOUND_MSG));
        genre.setName(dto.getName());
        Genre updatedGenre = genreRepository.save(genre);
        return genreMapper.toDto(updatedGenre);
    }
}
