package pavel.lab.cinema.dto.error;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String message,
        LocalDateTime time
) {}
