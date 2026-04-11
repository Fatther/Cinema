package pavel.lab.cinema.service.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class TaskService {

    private final Map<String, String> taskStatus = new ConcurrentHashMap<>();

    @Async
    public CompletableFuture<Void> generateReport(String taskId) {
        String message;
        try {
            taskStatus.put(taskId, "IN PROGRESS");
            message = "Задача " + taskId + " запущена в потоке " + Thread.currentThread().getName();
            log.info(message);
            Thread.sleep(15000);
            taskStatus.put(taskId, "FINISHED");
            message = "Задача " + taskId + " успешно завершена";
            log.info(message);
        } catch (InterruptedException _) {
            taskStatus.put(taskId, "ERROR");
            message = "Ошибка при выполнении задачи " + taskId;
            log.info(message);
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture(null);
    }

    public String getStatus(String taskId) {
            return taskStatus.getOrDefault(taskId, "NOT FOUND");
    }
}
