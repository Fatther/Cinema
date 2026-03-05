package pavel.lab.cinema.mapper;

import org.springframework.stereotype.Component;
import pavel.lab.cinema.dto.defaultdto.VisitorDTO;
import pavel.lab.cinema.dto.requestdto.VisitorRequestDTO;
import pavel.lab.cinema.entity.Visitor;

@Component
public class VisitorMapper {
    public VisitorDTO toDto(Visitor entity) {
        if (entity == null) return null;
        return VisitorDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }

    public Visitor toEntity(VisitorRequestDTO dto) {
        if (dto == null) return null;
        return Visitor.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }
}
