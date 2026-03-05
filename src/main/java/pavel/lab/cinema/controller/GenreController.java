package pavel.lab.cinema.controller;

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
    public GenreDTO create(@RequestBody GenreRequestDTO dto) {
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
        try {
            return genreService.findById(id);
        } catch (Exception _) {
            return null;
        }

    }

    @PutMapping("/update/{id}")
    public GenreDTO update(
            @PathVariable Long id,
            @RequestBody GenreRequestDTO dto
    ) {
        try {
            return genreService.update(id, dto);
        } catch (Exception _) {
            return null;
        }
    }

    @DeleteMapping("/delete/{id}")
    public void delete(
            @PathVariable Long id
    ) {
        genreService.delete(id);
    }
}
