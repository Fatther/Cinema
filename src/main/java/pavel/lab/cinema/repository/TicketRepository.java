package pavel.lab.cinema.repository;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pavel.lab.cinema.entity.Ticket;

import java.util.Optional;

@NullMarked
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @EntityGraph(attributePaths = {"session", "session.movie", "session.hall", "visitor"})
    Optional<Ticket> findGoodById(Long id);

    @Query(value = "SELECT t FROM Ticket t "
            + "LEFT JOIN FETCH t.session s "
            + "LEFT JOIN FETCH s.movie "
            + "LEFT JOIN FETCH s.hall "
            + "LEFT JOIN FETCH t.visitor",
            countQuery = "SELECT COUNT(t) FROM Ticket t")
    Page<Ticket> findAll(Pageable pageable);

    @Query(value = "SELECT t.* FROM tickets t "
    + "LEFT JOIN visitors v ON t.visitor_id = v.id "
    + "WHERE v.name LIKE CONCAT('%', :name, '%')",
            countQuery = "SELECT COUNT(*) FROM tickets t "
            + "JOIN visitors v ON t.visitor_id=v.id "
            + "WHERE v.name LIKE CONCAT('%', :name, '%')",
            nativeQuery = true)
    Page<Ticket> findTicketsByVisitor(String name, Pageable pageable);

    @Query(value = "SELECT t FROM Ticket t "
    + "LEFT JOIN FETCH t.session s "
    + "LEFT JOIN FETCH s.movie "
    + "LEFT JOIN FETCH s.hall "
    + "LEFT JOIN FETCH t.visitor v "
    + "WHERE v.name LIKE %:name%",
    countQuery = "SELECT COUNT(t) from Ticket t "
    + "JOIN t.visitor v WHERE v.name LIKE %:name%")
    Page<Ticket> findTicketsByVisitorJPQL(String name, Pageable pageable);
}
