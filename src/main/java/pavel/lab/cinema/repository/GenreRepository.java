package pavel.lab.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pavel.lab.cinema.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
