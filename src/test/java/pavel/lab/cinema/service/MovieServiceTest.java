package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pavel.lab.cinema.dto.defaultdto.MovieDTO;
import pavel.lab.cinema.dto.page.PageResponse;
import pavel.lab.cinema.dto.requestdto.MovieRequestDTO;
import pavel.lab.cinema.entity.Genre;
import pavel.lab.cinema.entity.Movie;
import pavel.lab.cinema.mapper.MovieMapper;
import pavel.lab.cinema.repository.GenreRepository;
import pavel.lab.cinema.repository.MovieRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie movie;
    private MovieDTO movieDTO;
    private MovieRequestDTO movieRequestDTO;
    private Genre genre;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        genre = new Genre();
        genre.setId(1L);
        genre.setName("Боевик");

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Интерстеллар");
        movie.setDuration(169);
        movie.setGenres(List.of(genre));

        movieDTO = new MovieDTO();
        movieDTO.setId(1L);
        movieDTO.setTitle("Интерстеллар");

        movieRequestDTO = new MovieRequestDTO();
        movieRequestDTO.setTitle("Интерстеллар");
        movieRequestDTO.setDuration(169);
        movieRequestDTO.setGenreIds(List.of(1L));

        pageable = PageRequest.of(0, 10);
    }
    @Test
    void create_savesMovieAndReturnsDto() {
        when(movieMapper.toEntity(movieRequestDTO)).thenReturn(movie);
        when(genreRepository.findAllById(List.of(1L))).thenReturn(List.of(genre));
        when(movieRepository.save(movie)).thenReturn(movie);
        when(movieMapper.toDto(movie)).thenReturn(movieDTO);

        MovieDTO result = movieService.create(movieRequestDTO);

        assertThat(result).isEqualTo(movieDTO);
        verify(movieRepository).save(movie);
    }

    @Test
    void create_throwsWhenGenresMismatch() {
        when(movieMapper.toEntity(movieRequestDTO)).thenReturn(movie);
        when(genreRepository.findAllById(List.of(1L))).thenReturn(List.of());

        assertThatThrownBy(() -> movieService.create(movieRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Ошибка в списке жанров");

        verify(movieRepository, never()).save(any());
    }
    @Test
    void findAll_returnsPageFromRepository_whenCacheMiss() {
        Page<Movie> page = new PageImpl<>(List.of(movie), pageable, 1);
        when(movieRepository.findAll(pageable)).thenReturn(page);
        when(movieMapper.toDto(movie)).thenReturn(movieDTO);

        PageResponse<MovieDTO> result = movieService.findAll(pageable);

        assertThat(result).isNotNull();
        verify(movieRepository).findAll(pageable);
    }

    @Test
    void findAll_returnsCachedResult_onSecondCall() {
        Page<Movie> page = new PageImpl<>(List.of(movie), pageable, 1);
        when(movieRepository.findAll(pageable)).thenReturn(page);
        when(movieMapper.toDto(movie)).thenReturn(movieDTO);

        movieService.findAll(pageable);
        movieService.findAll(pageable);

        verify(movieRepository, times(1)).findAll(pageable);
    }
    @Test
    void findById_returnsDto() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieMapper.toDto(movie)).thenReturn(movieDTO);

        assertThat(movieService.findById(1L)).isEqualTo(movieDTO);
    }

    @Test
    void findById_throwsWhenNotFound() {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Фильм с ID 99 не найден");
    }
    @Test
    void update_updatesFieldsAndReturnsDto() {
        MovieRequestDTO updateReq = new MovieRequestDTO();
        updateReq.setTitle("Дюна");
        updateReq.setDuration(155);
        updateReq.setGenreIds(List.of(1L));

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(genreRepository.findAllById(List.of(1L))).thenReturn(List.of(genre));
        when(movieRepository.save(movie)).thenReturn(movie);
        when(movieMapper.toDto(movie)).thenReturn(movieDTO);

        MovieDTO result = movieService.update(1L, updateReq);

        assertThat(movie.getTitle()).isEqualTo("Дюна");
        assertThat(movie.getDuration()).isEqualTo(155);
        assertThat(result).isEqualTo(movieDTO);
    }

    @Test
    void update_throwsWhenMovieNotFound() {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.update(99L, movieRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Фильм с ID 99 не найден");

        verify(movieRepository, never()).save(any());
    }

    @Test
    void update_throwsWhenGenresMismatch() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(genreRepository.findAllById(List.of(1L))).thenReturn(List.of());

        assertThatThrownBy(() -> movieService.update(1L, movieRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Ошибка в списке жанров");

        verify(movieRepository, never()).save(any());
    }
    @Test
    void delete_removesMovieAndClearsCache() {
        Page<Movie> page = new PageImpl<>(List.of(movie), pageable, 1);
        when(movieRepository.findAll(pageable)).thenReturn(page);
        when(movieMapper.toDto(movie)).thenReturn(movieDTO);
        movieService.findAll(pageable);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        movieService.delete(1L);

        verify(movieRepository).delete(movie);
        when(movieRepository.findAll(pageable)).thenReturn(page);
        movieService.findAll(pageable);
        verify(movieRepository, times(2)).findAll(pageable);
    }

    @Test
    void delete_throwsWhenNotFound() {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Фильм с ID 99 не найден");

        verify(movieRepository, never()).delete(any());
    }
}