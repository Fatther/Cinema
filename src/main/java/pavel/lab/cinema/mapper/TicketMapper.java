package pavel.lab.cinema.mapper;

import org.springframework.stereotype.Component;
import pavel.lab.cinema.dto.defaultdto.TicketDTO;
import pavel.lab.cinema.dto.requestdto.TicketRequestDTO;
import pavel.lab.cinema.entity.Ticket;

@Component
public class TicketMapper {
    public TicketDTO toDto(Ticket entity) {
        if (entity == null) {
            return null;
        }
        return TicketDTO.builder()
                .id(entity.getId())
                .seatNumber(entity.getSeatNumber())
                .movieTitle(entity.getSession().getMovie().getTitle())
                .startTime(entity.getSession().getStartTime())
                .visitorEmail(entity.getVisitor().getEmail())
                .build();
    }

    public Ticket toEntity(TicketRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Ticket.builder()
                .seatNumber(dto.getSeatNumber())
                .build();
    }
}
