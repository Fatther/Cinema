package pavel.lab.cinema.repository;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pavel.lab.cinema.entity.Movie;

import java.util.Optional;

@NullMarked
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT DISTINCT m from Movie m "
            + "LEFT JOIN FETCH m.genres "
            + "WHERE m.id = :id")
    Optional<Movie> findById(Long id);

    @Query(value = "SELECT DISTINCT m from Movie m "
            + "LEFT JOIN FETCH m.genres",
            countQuery = "SELECT COUNT(m) FROM Movie m"
    )
    Page<Movie> findAll(Pageable pageable);

}
