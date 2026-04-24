package pavel.lab.cinema.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pavel.lab.cinema.dto.defaultdto.TicketDTO;
import pavel.lab.cinema.dto.requestdto.TicketRequestDTO;
import pavel.lab.cinema.entity.Ticket;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    private final SessionMapper sessionMapper;

    public TicketDTO toDto(Ticket entity) {
        if (entity == null) {
            return null;
        }
        return TicketDTO.builder()
                .id(entity.getId())
                .seatNumber(entity.getSeatNumber())
                .session(sessionMapper.toDto(entity.getSession()))
                .visitorEmail(entity.getVisitor().getName() + " (" + entity.getVisitor().getEmail() + ")")
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
