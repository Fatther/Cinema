package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pavel.lab.cinema.dto.defaultdto.GenreDTO;
import pavel.lab.cinema.dto.requestdto.GenreRequestDTO;
import pavel.lab.cinema.entity.Genre;
import pavel.lab.cinema.entity.Movie;
import pavel.lab.cinema.mapper.GenreMapper;
import pavel.lab.cinema.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @InjectMocks
    private GenreService genreService;

    private Genre genre;
    private GenreDTO genreDTO;
    private GenreRequestDTO genreRequestDTO;

    @BeforeEach
    void setUp() {
        genre = new Genre();
        genre.setId(1L);
        genre.setName("Боевик");
        genre.setMovies(new ArrayList<>());

        genreDTO = new GenreDTO();
        genreDTO.setId(1L);
        genreDTO.setName("Боевик");

        genreRequestDTO = new GenreRequestDTO();
        genreRequestDTO.setName("Боевик");
    }
    @Test
    void create_savesAndReturnsDto() {
        when(genreMapper.toEntity(genreRequestDTO)).thenReturn(genre);
        when(genreRepository.save(genre)).thenReturn(genre);
        when(genreMapper.toDto(genre)).thenReturn(genreDTO);

        GenreDTO result = genreService.create(genreRequestDTO);

        assertThat(result).isEqualTo(genreDTO);
        verify(genreRepository).save(genre);
        verify(genreMapper).toDto(genre);
    }
    @Test
    void findAll_returnsMappedList() {
        when(genreRepository.findAll()).thenReturn(List.of(genre));
        when(genreMapper.toDto(genre)).thenReturn(genreDTO);

        List<GenreDTO> result = genreService.findAll();

        assertThat(result).containsExactly(genreDTO);
    }

    @Test
    void findAll_returnsEmptyList() {
        when(genreRepository.findAll()).thenReturn(List.of());

        assertThat(genreService.findAll()).isEmpty();
        verifyNoInteractions(genreMapper);
    }
    @Test
    void findById_returnsDto() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(genreMapper.toDto(genre)).thenReturn(genreDTO);

        assertThat(genreService.findById(1L)).isEqualTo(genreDTO);
    }

    @Test
    void findById_throwsWhenNotFound() {
        when(genreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> genreService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Жанр с ID 99 не найден");
    }
    @Test
    void delete_removesGenre_whenNoMovies() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        genreService.delete(1L);

        verify(genreRepository).delete(genre);
    }

    @Test
    void delete_removesGenreFromMovies_beforeDeleting() {
        Movie movie1 = new Movie();
        movie1.setGenres(new ArrayList<>(List.of(genre)));
        Movie movie2 = new Movie();
        movie2.setGenres(new ArrayList<>(List.of(genre)));
        genre.setMovies(new ArrayList<>(List.of(movie1, movie2)));

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        genreService.delete(1L);

        assertThat(movie1.getGenres()).doesNotContain(genre);
        assertThat(movie2.getGenres()).doesNotContain(genre);
        verify(genreRepository).delete(genre);
    }

    @Test
    void delete_throwsWhenNotFound() {
        when(genreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> genreService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Жанр с ID 99 не найден");

        verify(genreRepository, never()).delete(any());
    }
    @Test
    void update_updatesNameAndReturnsDto() {
        GenreRequestDTO updateReq = new GenreRequestDTO();
        updateReq.setName("Комедия");

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(genreRepository.save(genre)).thenReturn(genre);
        when(genreMapper.toDto(genre)).thenReturn(genreDTO);

        GenreDTO result = genreService.update(1L, updateReq);

        assertThat(genre.getName()).isEqualTo("Комедия");
        assertThat(result).isEqualTo(genreDTO);
        verify(genreRepository).save(genre);
    }

    @Test
    void update_throwsWhenNotFound() {
        when(genreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> genreService.update(99L, genreRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Жанр с ID 99 не найден");

        verify(genreRepository, never()).save(any());
    }
}