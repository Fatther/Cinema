package pavel.lab.cinema.repository;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pavel.lab.cinema.entity.Ticket;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @EntityGraph(attributePaths = {"session", "session.movie", "session.hall", "visitor"})
    Optional<Ticket> findGoodById(Long id);

    @EntityGraph(attributePaths = {"session", "session.movie", "session.hall", "visitor"})
    List<Ticket> findAll();

    @Query(value = "SELECT t.* FROM tickets t "
    + "LEFT JOIN visitors v ON t.visitor_id = v.id "
    + "WHERE v.name LIKE CONCAT('%', :name, '%')",
            nativeQuery = true)
    List<Ticket> findTicketsByVisitor(String name);

    @Query("SELECT t FROM Ticket t "
    + "LEFT JOIN FETCH t.session s "
    + "LEFT JOIN FETCH s.movie "
    + "LEFT JOIN FETCH s.hall "
    + "LEFT JOIN FETCH t.visitor v "
    + "WHERE v.name LIKE %:name%")
    List<Ticket> findTicketsByVisitorJPQL(String name);
}
