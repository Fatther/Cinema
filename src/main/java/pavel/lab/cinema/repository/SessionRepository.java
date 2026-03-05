package pavel.lab.cinema.repository;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pavel.lab.cinema.entity.Session;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("SELECT DISTINCT s FROM Session s LEFT JOIN FETCH s.movie WHERE s.id = :id")
    Optional<Session> findById(Long id);

    @Query("SELECT DISTINCT s FROM Session s LEFT JOIN FETCH s.movie")
    List<Session> findAll();
}
