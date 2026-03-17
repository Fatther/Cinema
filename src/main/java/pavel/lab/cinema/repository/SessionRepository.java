package pavel.lab.cinema.repository;

import org.springframework.data.domain.Page;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pavel.lab.cinema.entity.Session;

import java.util.Optional;

@NullMarked
public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("SELECT DISTINCT s FROM Session s"
            + " LEFT JOIN FETCH s.movie"
            + " LEFT JOIN FETCH s.hall"
            + " WHERE s.id = :id")
    Optional<Session> findById(Long id);

    @Query(value = "SELECT DISTINCT s FROM Session s"
            + " LEFT JOIN FETCH s.movie"
            + " LEFT JOIN FETCH s.hall",
    countQuery = "SELECT COUNT(s) FROM Session s")
    Page<Session> findAll(Pageable pageable);

    @Query(value = "SELECT s FROM Session s "
            + "LEFT JOIN FETCH s.hall "
            + "LEFT JOIN FETCH s.movie m"
            + " WHERE m.title LIKE %:title%",
            countQuery = "SELECT COUNT(s) FROM Session s JOIN s.movie m WHERE m.title LIKE %:title%")
    Page<Session> findSessionByMovie(@Param("title") String title, Pageable pageable);

    @Query(value = "SELECT s.* FROM sessions s "
            + "LEFT JOIN movies m ON s.movie_id = m.id "
            + "LEFT JOIN halls h ON s.hall_id = h.id "
            + "WHERE m.title LIKE CONCAT('%', :title, '%')",
            countQuery = "SELECT COUNT(*) FROM sessions s "
                    + "JOIN movies m ON s.movie_id = m.id "
                    + "WHERE m.title LIKE CONCAT('%', :title, '%')",
            nativeQuery = true)
    Page<Session> findSessionByMovieNative(@Param("title") String title, Pageable pageable);
}
