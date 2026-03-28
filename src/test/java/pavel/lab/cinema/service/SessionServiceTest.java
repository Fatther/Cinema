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
import pavel.lab.cinema.dto.defaultdto.SessionDTO;
import pavel.lab.cinema.dto.page.PageResponse;
import pavel.lab.cinema.dto.requestdto.SessionRequestDTO;
import pavel.lab.cinema.entity.Hall;
import pavel.lab.cinema.entity.Movie;
import pavel.lab.cinema.entity.Session;
import pavel.lab.cinema.mapper.SessionMapper;
import pavel.lab.cinema.repository.HallRepository;
import pavel.lab.cinema.repository.MovieRepository;
import pavel.lab.cinema.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private SessionMapper sessionMapper;
    @Mock private MovieRepository movieRepository;
    @Mock private HallRepository hallRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private SessionDTO sessionDTO;
    private SessionRequestDTO sessionRequestDTO;
    private Movie movie;
    private Hall hall;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Интерстеллар");

        hall = new Hall();
        hall.setId(1L);
        hall.setName("Зал 1");

        session = new Session();
        session.setId(1L);
        session.setMovie(movie);
        session.setHall(hall);
        session.setStartTime(LocalDateTime.now());

        sessionDTO = new SessionDTO();
        sessionDTO.setId(1L);

        sessionRequestDTO = new SessionRequestDTO();
        sessionRequestDTO.setMovieId(1L);
        sessionRequestDTO.setHallId(1L);
        sessionRequestDTO.setStartTime(LocalDateTime.now());

        pageable = PageRequest.of(0, 10);
    }
    @Test
    void findAll_returnsPage_onCacheMiss() {
        Page<Session> page = new PageImpl<>(List.of(session), pageable, 1);
        when(sessionRepository.findAll(pageable)).thenReturn(page);
        when(sessionMapper.toDto(session)).thenReturn(sessionDTO);

        PageResponse<SessionDTO> result = sessionService.findAll(pageable);

        assertThat(result).isNotNull();
        verify(sessionRepository).findAll(pageable);
    }

    @Test
    void findAll_returnsCached_onSecondCall() {
        Page<Session> page = new PageImpl<>(List.of(session), pageable, 1);
        when(sessionRepository.findAll(pageable)).thenReturn(page);
        when(sessionMapper.toDto(session)).thenReturn(sessionDTO);

        sessionService.findAll(pageable);
        sessionService.findAll(pageable);

        verify(sessionRepository, times(1)).findAll(pageable);
    }
    @Test
    void create_savesSessionAndReturnsDto() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(sessionMapper.toEntity(sessionRequestDTO)).thenReturn(session);
        when(sessionRepository.save(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDTO);

        SessionDTO result = sessionService.create(sessionRequestDTO);

        assertThat(result).isEqualTo(sessionDTO);
        verify(sessionRepository).save(session);
    }

    @Test
    void create_throwsWhenMovieNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.create(sessionRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Фильм с ID 1 не найден(а)");

        verifyNoInteractions(hallRepository, sessionRepository);
    }

    @Test
    void create_throwsWhenHallNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(hallRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.create(sessionRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Зал с ID 1 не найден(а)");

        verify(sessionRepository, never()).save(any());
    }
    @Test
    void update_updatesAndReturnsDto() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(sessionMapper.toDto(session)).thenReturn(sessionDTO);

        SessionDTO result = sessionService.update(1L, sessionRequestDTO);

        assertThat(result).isEqualTo(sessionDTO);
        verify(sessionRepository).findById(1L);
    }

    @Test
    void update_throwsWhenSessionNotFound() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.update(99L, sessionRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Сессия с ID 99 не найден(а)");
    }

    @Test
    void update_throwsWhenMovieNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.update(1L, sessionRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Фильм с ID 1 не найден(а)");
    }

    @Test
    void update_throwsWhenHallNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(hallRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.update(1L, sessionRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Зал с ID 1 не найден(а)");
    }
    @Test
    void findById_returnsDto() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionMapper.toDto(session)).thenReturn(sessionDTO);

        assertThat(sessionService.findById(1L)).isEqualTo(sessionDTO);
    }

    @Test
    void findById_throwsWhenNotFound() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Сессия с ID 99 не найден(а)");
    }
    @Test
    void delete_removesSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        sessionService.delete(1L);

        verify(sessionRepository).delete(session);
    }

    @Test
    void delete_throwsWhenNotFound() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Сессия с ID 99 не найден(а)");

        verify(sessionRepository, never()).delete(any());
    }
    @Test
    void saveMultipleUnsafe_savesAll() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(sessionMapper.toEntity(sessionRequestDTO)).thenReturn(session);
        when(sessionRepository.save(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDTO);

        List<SessionDTO> result = sessionService.saveMultipleUnsafe(List.of(sessionRequestDTO));

        assertThat(result).containsExactly(sessionDTO);
    }

    @Test
    void saveMultipleUnsafe_throwsWhenMovieNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.saveMultipleUnsafe(List.of(sessionRequestDTO)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Фильм с ID 1 не найден(а)");
    }

    @Test
    void saveMultipleUnsafe_throwsWhenHallNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(hallRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.saveMultipleUnsafe(List.of(sessionRequestDTO)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Зал с ID 1 не найден(а)");
    }
    @Test
    void saveMultipleSafe_delegatesToUnsafe() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(sessionMapper.toEntity(sessionRequestDTO)).thenReturn(session);
        when(sessionRepository.save(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDTO);

        List<SessionDTO> result = sessionService.saveMultipleSafe(List.of(sessionRequestDTO));

        assertThat(result).containsExactly(sessionDTO);
    }
    @Test
    void findSessionByMovie_returnsPage_onCacheMiss() {
        Page<Session> page = new PageImpl<>(List.of(session), pageable, 1);
        when(sessionRepository.findSessionByMovie("Интерстеллар", pageable)).thenReturn(page);
        when(sessionMapper.toDto(session)).thenReturn(sessionDTO);

        PageResponse<SessionDTO> result = sessionService.findSessionByMovie("Интерстеллар", pageable);

        assertThat(result).isNotNull();
        verify(sessionRepository).findSessionByMovie("Интерстеллар", pageable);
    }

    @Test
    void findSessionByMovie_returnsCached_onSecondCall() {
        Page<Session> page = new PageImpl<>(List.of(session), pageable, 1);
        when(sessionRepository.findSessionByMovie("Интерстеллар", pageable)).thenReturn(page);
        when(sessionMapper.toDto(session)).thenReturn(sessionDTO);

        sessionService.findSessionByMovie("Интерстеллар", pageable);
        sessionService.findSessionByMovie("Интерстеллар", pageable);

        verify(sessionRepository, times(1)).findSessionByMovie("Интерстеллар", pageable);
    }
    @Test
    void findSessionByMovieNative_returnsPage_onCacheMiss() {
        Page<Session> page = new PageImpl<>(List.of(session), pageable, 1);
        when(sessionRepository.findSessionByMovieNative("Интерстеллар", pageable)).thenReturn(page);
        when(sessionMapper.toDto(session)).thenReturn(sessionDTO);

        PageResponse<SessionDTO> result = sessionService.findSessionByMovieNative("Интерстеллар", pageable);

        assertThat(result).isNotNull();
        verify(sessionRepository).findSessionByMovieNative("Интерстеллар", pageable);
    }

    @Test
    void findSessionByMovieNative_returnsCached_onSecondCall() {
        Page<Session> page = new PageImpl<>(List.of(session), pageable, 1);
        when(sessionRepository.findSessionByMovieNative("Интерстеллар", pageable)).thenReturn(page);
        when(sessionMapper.toDto(session)).thenReturn(sessionDTO);

        sessionService.findSessionByMovieNative("Интерстеллар", pageable);
        sessionService.findSessionByMovieNative("Интерстеллар", pageable);

        verify(sessionRepository, times(1)).findSessionByMovieNative("Интерстеллар", pageable);
    }
}