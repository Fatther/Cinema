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
                .movieTitle(entity.getMovie() != null ? entity.getMovie().getTitle() : null)
                .hallName(entity.getHall() != null ? entity.getHall().getName() : null)
                .price(entity.getHall() != null ? entity.getHall().getPrice() : 0.0) // Цена из зала
                .build();
    }

    public Session toEntity(SessionRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Session.builder()
                .startTime(dto.getStartTime())
                .build();
    }
}