package pavel.lab.cinema.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import pavel.lab.cinema.entities.Movie;

@Repository
public class CinemaRepository {
    private final Map<Long, Movie> movies;

    public CinemaRepository() {
        movies = new HashMap<>();
        movies.put(1L, new Movie(1L, "Avatar", 200, "12+", "Sci-Fi"));
        movies.put(2L, new Movie(2L, "Inception", 150, "12+", "Thriller"));
        movies.put(3L, new Movie(3L, "Terminator", 100, "16+", "Action"));
        movies.put(4L, new Movie(4L, "Transporter", 90, "16+", "Action"));
        movies.put(5L, new Movie(5L, "Cars", 110, "0+", "Animated"));
    }

    public Movie findMovieById(
            Long id
    ) {
        if (!movies.containsKey(id)) {
            return null;
        }
        return movies.get(id);
    }

    public List<Movie> findAllMovies() {
        return new ArrayList<>(movies.values());
    }
}
