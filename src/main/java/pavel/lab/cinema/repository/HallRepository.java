package pavel.lab.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pavel.lab.cinema.entity.Hall;

public interface HallRepository extends JpaRepository<Hall, Long> {
}
