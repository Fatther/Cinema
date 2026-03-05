package pavel.lab.cinema.mapper;

import org.springframework.stereotype.Component;
import pavel.lab.cinema.dto.defaultdto.SessionDTO;
import pavel.lab.cinema.dto.requestdto.SessionRequestDTO;
import pavel.lab.cinema.entity.Session;

@Component
public class SessionMapper {
    public SessionDTO toDto(Session entity) {
        if (entity == null) {
            return null;
        }
        return SessionDTO.builder()
                .id(entity.getId())
                .startTime(entity.getStartTime())
                .price(entity.getPrice())
                .movie(entity.getMovie() != null ? entity.getMovie().getTitle() : null)
                .build();
    }

    public Session toEntity(SessionRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Session.builder()
                .startTime(dto.getStartTime())
                .price(dto.getPrice())
                .build();
    }
}
