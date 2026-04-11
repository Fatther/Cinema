package pavel.lab.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CinemaApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }
}
