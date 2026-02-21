package pavel.lab.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CinemaApplication {
    private CinemaApplication() {
    }

    static void main(final String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }
}
