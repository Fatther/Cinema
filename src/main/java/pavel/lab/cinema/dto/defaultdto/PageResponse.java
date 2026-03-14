package pavel.lab.cinema.dto.defaultdto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {
    private List<T> content;
    private Metadata metadata;

    public record Metadata(
            int page,
            int totalPage,
            int size,
            long totalElements
    ) {}

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.metadata = new Metadata(
                page.getNumber(),
                page.getTotalPages(),
                page.getSize(),
                page.getTotalElements()
        );
    }
}
