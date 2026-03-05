package pavel.lab.cinema.mapper;

import org.springframework.stereotype.Component;
import pavel.lab.cinema.dto.defaultdto.GenreDTO;
import pavel.lab.cinema.dto.requestdto.GenreRequestDTO;
import pavel.lab.cinema.entity.Genre;

@Component
public class GenreMapper {

    public GenreDTO toDto(Genre entity) {
        if (entity == null) return null;

        return GenreDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public Genre toEntity(GenreRequestDTO dto) {
        if (dto == null) return null;
        return Genre.builder()
                .name(dto.getName())
                .build();
    }
}