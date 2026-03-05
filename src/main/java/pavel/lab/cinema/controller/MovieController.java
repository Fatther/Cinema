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
import pavel.lab.cinema.dto.defaultdto.MovieDTO;
import pavel.lab.cinema.dto.requestdto.MovieRequestDTO;
import pavel.lab.cinema.service.MovieService;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping("/post")
    public MovieDTO create(
            @RequestBody MovieRequestDTO dto
            ) {
        try {
            return movieService.create(dto);
        } catch (Exception _) {
            return null;
        }
    }

    @GetMapping
    public List<MovieDTO> findAll() {
        return movieService.findAll();
    }

    @GetMapping("/{id}")
    public MovieDTO findById(@PathVariable Long id)  {
        return movieService.findById(id);
    }

    @PutMapping("/update/{id}")
    public MovieDTO update(@PathVariable Long id, @RequestBody MovieRequestDTO dto) {
        try {
            return movieService.update(id, dto);
        } catch (Exception _) {
            return null;
        }
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        movieService.delete(id);
    }

}
