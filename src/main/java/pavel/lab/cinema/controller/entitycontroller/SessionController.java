package pavel.lab.cinema.controller.entitycontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pavel.lab.cinema.dto.defaultdto.SessionDTO;
import pavel.lab.cinema.dto.page.PageResponse;
import pavel.lab.cinema.dto.requestdto.SessionRequestDTO;
import pavel.lab.cinema.service.SessionService;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
@Validated
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/post")
    public SessionDTO create(@Valid @RequestBody SessionRequestDTO dto) {
        return sessionService.create(dto);
    }

    @GetMapping
    public PageResponse<SessionDTO> findAll(@PageableDefault(size = 3) Pageable pageable) {
        return sessionService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public SessionDTO findById(@PathVariable Long id) {
        return sessionService.findById(id);
    }

    @PutMapping("/update/{id}")
    public SessionDTO update(@PathVariable Long id, @Valid @RequestBody SessionRequestDTO dto) {
        return sessionService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        sessionService.delete(id);
    }

    @PostMapping("/post/bulk/unsafe")
    public List<SessionDTO> createMultipleUnsafe(@RequestBody @Valid List<SessionRequestDTO> dtos) {
        return sessionService.saveMultipleUnsafe(dtos);
    }

    @PostMapping("/post/bulk/safe")
    public List<SessionDTO> createMultipleSafe(@RequestBody @Valid List<SessionRequestDTO> dtos) {
        return sessionService.saveMultipleSafe(dtos);
    }

    @GetMapping("/search")
    public PageResponse<SessionDTO> findSessionByMovie(@RequestParam String title,
                                                       @PageableDefault(size = 3) Pageable pageable) {
        return sessionService.findSessionByMovie(title, pageable);
    }

    @GetMapping("/nativesearch")
    public PageResponse<SessionDTO> findSessionByMovieNative(@RequestParam String title,
                                                     @PageableDefault(size = 3) Pageable pageable) {
        return sessionService.findSessionByMovieNative(title, pageable);
    }
}
