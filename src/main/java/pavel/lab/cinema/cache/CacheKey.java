package pavel.lab.cinema.cache;

import org.springframework.data.domain.Pageable;

public record CacheKey(
        int page,
        int size,
        String sort,
        String unique
) {
    public CacheKey(Pageable pageable) {
        this(pageable, "All");
    }

    public CacheKey(Pageable pageable, String unique) {
        this(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().toString(),
                unique
        );
    }
}
