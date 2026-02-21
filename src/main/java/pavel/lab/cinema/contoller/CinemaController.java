package pavel.lab.cinema.contoller;


import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import pavel.lab.cinema.dto.MovieDto;
import pavel.lab.cinema.sevice.CinemaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class CinemaController {
    public final CinemaService cinemaService;

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> findMovieByID(
            @PathVariable Long id
    ) {
        try {
        return ResponseEntity.ok(cinemaService.findMovieByID(id));
        }
        catch (Exception _) {
            return ResponseEntity.status(404)
                    .build();
            }
        }

    @GetMapping("/filter")
    public List<MovieDto> findMoviesByDuration(
            @RequestParam(defaultValue = "120") Integer maxDuration
    ) {
        return cinemaService.findMoviesByDuration(maxDuration);
    }

    @GetMapping()
    public List<MovieDto> findAllMovies() {
        return cinemaService.findAllMovies();
        }
    }



