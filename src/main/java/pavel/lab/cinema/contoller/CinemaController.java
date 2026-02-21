package pavel.lab.cinema.contoller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pavel.lab.cinema.dto.MovieDto;
import pavel.lab.cinema.sevice.CinemaService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class CinemaController {
    private final CinemaService cinemaService;

    @GetMapping("/{id}")
    public MovieDto findMovieById(
            @PathVariable final Long id
    ) {
        return cinemaService.findMovieById(id);
    }

    @GetMapping("/filter")
    public List<MovieDto> findMoviesByDuration(
            @RequestParam(defaultValue = "120") final Integer maxDuration
    ) {
        return cinemaService.findMoviesByDuration(maxDuration);
    }

    @GetMapping()
    public List<MovieDto> findAllMovies() {
        return cinemaService.findAllMovies();
    }
}



