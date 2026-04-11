package pavel.lab.cinema.service.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class CounterService {
    private final AtomicInteger atomicCounter = new AtomicInteger(0);

    public void raceDemonstration() throws InterruptedException {
        int threadsNumber = 75;
        int incNumber = 10000;
        int goalNumber = threadsNumber * incNumber;

        int[] unsafeCounter = {0};
        atomicCounter.set(0);

        log.info("Старт демонстрации на {} потоков", threadsNumber);

        try (ExecutorService executor = Executors.newFixedThreadPool(threadsNumber)) {
            CountDownLatch latch = new CountDownLatch(goalNumber);

            for (int i = 0; i < goalNumber; i++) {
                executor.submit(() -> {
                    try {
                        unsafeCounter[0]++;
                        atomicCounter.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
        }

        log.info("Ожидаемое значение: {}", goalNumber);
        log.info("Результат UNSAFE: {}", unsafeCounter[0]);
        log.info("Результат ATOMIC: {}", atomicCounter.get());
    }
}
