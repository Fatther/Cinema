package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pavel.lab.cinema.cache.CacheKey;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final Map<CacheKey, PageResponse<SessionDTO>> sessionCache = new HashMap<>();

    private static final String NOT_FOUND_MSG = " не найден(а)";
    private static final String MOVIE_PREFIX = "Фильм с ID ";
    private static final String SESSION_PREFIX = "Сессия с ID ";
    private static final String HALL_PREFIX = "Зал с ID ";

    @Transactional(readOnly = true)
    public PageResponse<SessionDTO> findAll(Pageable pageable) {
        CacheKey key = new CacheKey(pageable);
        if (sessionCache.containsKey(key)) {
            return sessionCache.get(key);
        }
        Page<Session> sessionsPage = sessionRepository.findAll(pageable);
        PageResponse<SessionDTO> dtosPage = new PageResponse<>(sessionsPage.map(sessionMapper::toDto));
        sessionCache.put(key, dtosPage);
        return dtosPage;
    }

    @Transactional
    public SessionDTO create(SessionRequestDTO dto) {
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException(MOVIE_PREFIX + dto.getMovieId() + NOT_FOUND_MSG));
        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new EntityNotFoundException(HALL_PREFIX + dto.getMovieId() + NOT_FOUND_MSG));

        Session session = sessionMapper.toEntity(dto);
        session.setMovie(movie);
        session.setHall(hall);
        sessionCache.clear();
        log.info("Сессия на фильм " + movie.getTitle() + " создана");
        return sessionMapper.toDto(sessionRepository.save(session));
    }

    @Transactional
    public SessionDTO update(Long id, SessionRequestDTO dto) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(SESSION_PREFIX + id + NOT_FOUND_MSG));

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException(MOVIE_PREFIX + dto.getMovieId() + NOT_FOUND_MSG));
        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new EntityNotFoundException(HALL_PREFIX + dto.getMovieId() + NOT_FOUND_MSG));

        session.setStartTime(dto.getStartTime());
        session.setMovie(movie);
        session.setHall(hall);
        sessionCache.clear();
        log.info("Сессия с ID " + id + " обновлена");
        return sessionMapper.toDto(session);
    }

    @Transactional
    public SessionDTO findById(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(SESSION_PREFIX + id + NOT_FOUND_MSG));
        return sessionMapper.toDto(session);
    }

    @Transactional
    public void delete(Long id) {
        sessionRepository.deleteById(id);
        sessionCache.clear();
        log.info("Сессия с ID " + id + " удалена");
    }

    public List<SessionDTO> saveMultipleWithError(List<SessionRequestDTO> dtos) {
        List<SessionDTO> result = new ArrayList<>();
        int count = 0;

        for (SessionRequestDTO dto : dtos) {
            if (count == 2) {
                throw new EntityNotFoundException("Какая-то ошибка");
            }

            Session session = sessionMapper.toEntity(dto);
            Movie movie = movieRepository.findById(dto.getMovieId())
                    .orElseThrow(() -> new EntityNotFoundException(MOVIE_PREFIX + dto.getMovieId() + NOT_FOUND_MSG));

            session.setMovie(movie);
            Session savedSession = sessionRepository.save(session);

            result.add(sessionMapper.toDto(savedSession));

            count++;
        }
        return result;
    }

    @Transactional
    public List<SessionDTO> saveMultipleWithoutError(List<SessionRequestDTO> dtos) {
        return saveMultipleWithError(dtos);
    }

    @Transactional
    public PageResponse<SessionDTO> findSessionByMovie(String title, Pageable pageable) {
        CacheKey key = new CacheKey(pageable, title);
        if (sessionCache.containsKey(key)) {
            return sessionCache.get(key);
        }
        Page<Session> sessionsPage = sessionRepository.findSessionByMovie(title, pageable);
        PageResponse<SessionDTO> dtosPage = new PageResponse<>(sessionsPage.map(sessionMapper::toDto));
        sessionCache.put(key, dtosPage);
        return dtosPage;
    }

    @Transactional
    public PageResponse<SessionDTO> findSessionByMovieNative(String title, Pageable pageable) {
        CacheKey key = new CacheKey(pageable, title);
        if (sessionCache.containsKey(key)) {
            return sessionCache.get(key);
        }
        Page<Session> sessionsPage = sessionRepository.findSessionByMovieNative(title, pageable);
        PageResponse<SessionDTO> dtosPage = new PageResponse<>(sessionsPage.map(sessionMapper::toDto));
        sessionCache.put(key, dtosPage);
        return dtosPage;
    }

}
