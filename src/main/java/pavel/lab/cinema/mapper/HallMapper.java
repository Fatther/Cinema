package pavel.lab.cinema.mapper;

import org.springframework.stereotype.Component;
import pavel.lab.cinema.dto.requestdto.HallRequestDTO;
import pavel.lab.cinema.dto.defaultdto.HallDTO;
import pavel.lab.cinema.entity.Hall;

@Component
public class HallMapper {

    public HallDTO toDto(Hall entity) {
        if (entity == null) {
            return null;
        }

        return HallDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .seatAmount(entity.getSeatAmount())
                .build();
    }

    public Hall toEntity(HallRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Hall.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .seatAmount(dto.getSeatAmount())
                .build();
    }
}