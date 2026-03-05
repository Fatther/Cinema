package pavel.lab.cinema.repository;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pavel.lab.cinema.entity.Ticket;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @EntityGraph(attributePaths = {"session", "session.movie", "visitor"})
    Optional<Ticket> findGoodById(Long id);

    @EntityGraph(attributePaths = {"session", "session.movie", "visitor"})
    List<Ticket> findAll();
}
