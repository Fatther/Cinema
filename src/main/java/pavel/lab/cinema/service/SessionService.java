package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pavel.lab.cinema.dto.defaultdto.SessionDTO;
import pavel.lab.cinema.dto.requestdto.SessionRequestDTO;
import pavel.lab.cinema.entity.Hall;
import pavel.lab.cinema.entity.Movie;
import pavel.lab.cinema.entity.Session;
import pavel.lab.cinema.mapper.SessionMapper;
import pavel.lab.cinema.repository.HallRepository;
import pavel.lab.cinema.repository.MovieRepository;
import pavel.lab.cinema.repository.SessionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;

    private static final String NOT_FOUND_MSG = " not found";
    private static final String MOVIE_PREFIX = "Movie with ID ";
    private static final String SESSION_PREFIX = "Session with ID ";

    @Transactional(readOnly = true)
    public List<SessionDTO> findAll() {
        return sessionRepository.findAll().stream()
                .map(sessionMapper::toDto)
                .toList();
    }

    @Transactional
    public SessionDTO create(SessionRequestDTO dto) {
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new EntityNotFoundException("Hall not found"));

        Session session = sessionMapper.toEntity(dto);
        session.setMovie(movie);
        session.setHall(hall);

        return sessionMapper.toDto(sessionRepository.save(session));
    }

    @Transactional
    public SessionDTO update(Long id, SessionRequestDTO dto) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new EntityNotFoundException("Hall not found"));

        session.setStartTime(dto.getStartTime());
        session.setMovie(movie);
        session.setHall(hall);

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
    }

    public List<SessionDTO> saveMultipleWithError(List<SessionRequestDTO> dtos) {
        List<SessionDTO> result = new ArrayList<>();
        int count = 0;

        for (SessionRequestDTO dto : dtos) {
            if (count == 2) {
                throw new EntityNotFoundException("Some trouble");
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
    public List<SessionDTO> findSessionByMovie(String title) {
        List<Session> sessions = sessionRepository.findSessionByMovie(title);
        return sessions.stream()
                .map(sessionMapper::toDto)
                .toList();
    }

    @Transactional
    public List<SessionDTO> findSessionByMovieNative(String title) {
        List<Session> sessions = sessionRepository.findSessionByMovieNative(title);
        return sessions.stream()
                .map(sessionMapper::toDto)
                .toList();
    }

}
