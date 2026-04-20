package pavel.lab.cinema.controller.async;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pavel.lab.cinema.service.async.CounterService;

@RestController
@RequestMapping("/race")
@RequiredArgsConstructor
public class CounterController {

    private final CounterService counterService;

    @PostMapping("/demo")
    public ResponseEntity<String> raceDemo() {
        try {
            counterService.raceDemonstration();
            return ResponseEntity.ok("Демонстрация завершена, результат в логах");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError().body("Ошибка при выполнении теста");
        }
    }
}
