package pavel.lab.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pavel.lab.cinema.entity.Visitor;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
}
