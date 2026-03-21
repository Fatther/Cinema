package pavel.lab.cinema.controller.entitycontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pavel.lab.cinema.dto.defaultdto.GenreDTO;
import pavel.lab.cinema.dto.requestdto.GenreRequestDTO;
import pavel.lab.cinema.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping("/post")
    public GenreDTO create(@Valid @RequestBody GenreRequestDTO dto) {
        return genreService.create(dto);
    }

    @GetMapping
    public List<GenreDTO> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public GenreDTO findById(
            @PathVariable Long id
    ) {
        return genreService.findById(id);
    }

    @PutMapping("/update/{id}")
    public GenreDTO update(
            @PathVariable Long id,
            @Valid @RequestBody GenreRequestDTO dto
    ) {
        return genreService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(
            @PathVariable Long id
    ) {
        genreService.delete(id);
    }
}
