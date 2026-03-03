package pavel.lab.cinema.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pavel.lab.cinema.dto.MovieDto;
import pavel.lab.cinema.service.MovieService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> findMovieById(
            @PathVariable Long id
    ) {
        if (movieService.findMovieById(id) == null) {
            return ResponseEntity.status(404)
                    .build();
        }
        return ResponseEntity.ok(movieService.findMovieById(id));
    }

    @GetMapping("/filter")
    public List<MovieDto> findMoviesByDuration(
            @RequestParam(defaultValue = "120") final Integer maxDuration
    ) {
        return movieService.findMoviesByDuration(maxDuration);
    }

    @GetMapping()
    public List<MovieDto> findAllMovies() {
        return movieService.findAllMovies();
    }
}



