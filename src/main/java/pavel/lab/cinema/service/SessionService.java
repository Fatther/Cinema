package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pavel.lab.cinema.dto.defaultdto.SessionDTO;
import pavel.lab.cinema.dto.requestdto.SessionRequestDTO;
import pavel.lab.cinema.entity.Movie;
import pavel.lab.cinema.entity.Session;
import pavel.lab.cinema.mapper.SessionMapper;
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

    @Transactional
    public SessionDTO create(SessionRequestDTO dto) {
        Session session = sessionMapper.toEntity(dto);
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException("Movie with ID " + dto.getMovieId() + " not found"));
        session.setMovie(movie);
        Session createdSession = sessionRepository.save(session);
        return sessionMapper.toDto(createdSession);
    }

    @Transactional
    public List<SessionDTO> findAll() {
        List<Session> sessions = sessionRepository.findAll();
        return sessions.stream().map(sessionMapper::toDto).toList();
    }

    @Transactional
    public SessionDTO findById(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session with ID " + id + " not found"));
        return sessionMapper.toDto(session);
    }

    @Transactional
    public SessionDTO update(Long id, SessionRequestDTO dto) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session with ID " + id + " not found"));
        session.setStartTime(dto.getStartTime());
        session.setPrice(dto.getPrice());
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException("Movie with ID " + dto.getMovieId() + " not found"));
        session.setMovie(movie);
        Session updatedSession = sessionRepository.save(session);
        return sessionMapper.toDto(updatedSession);
    }

    @Transactional
    public void delete(Long id) {
        sessionRepository.deleteById(id);
    }

    public List<SessionDTO> saveMultipleWithError(List<SessionRequestDTO> dtos) {
        List<SessionDTO> result = new ArrayList<>();
        int count = 0;

        for (SessionRequestDTO dto : dtos) {
            count++;
            if (count == 2) {
                throw new RuntimeException("Some trouble");
            }

            Session session = sessionMapper.toEntity(dto);
            Movie movie = movieRepository.findById(dto.getMovieId())
                    .orElseThrow(() -> new EntityNotFoundException("Movie ID " + dto.getMovieId() + " not found"));

            session.setMovie(movie);
            Session savedSession = sessionRepository.save(session);

            result.add(sessionMapper.toDto(savedSession));
        }
        return result;
    }

    @Transactional
    public List<SessionDTO> saveMultipleWithoutError(List<SessionRequestDTO> dtos) {
        return saveMultipleWithError(dtos);
    }
}
