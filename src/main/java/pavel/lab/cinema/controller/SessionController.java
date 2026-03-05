package pavel.lab.cinema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pavel.lab.cinema.dto.defaultdto.SessionDTO;
import pavel.lab.cinema.dto.requestdto.SessionRequestDTO;
import pavel.lab.cinema.service.SessionService;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/post")
    public SessionDTO create(@RequestBody SessionRequestDTO dto) {
        return sessionService.create(dto);
    }

    @GetMapping
    public List<SessionDTO> findAll() {
        return sessionService.findAll();
    }

    @GetMapping("/{id}")
    public SessionDTO findById(@PathVariable Long id) {
        return sessionService.findById(id);
    }

    @PutMapping("/update/{id}")
    public SessionDTO update(@PathVariable Long id, @RequestBody SessionRequestDTO dto) {
        return sessionService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        sessionService.delete(id);
    }

    @PostMapping("/post/bad")
    public List<SessionDTO> createMultipleWithError(@RequestBody List<SessionRequestDTO> dtos) {
        return sessionService.saveMultipleWithError(dtos);
    }

    @PostMapping("/post/good")
    public List<SessionDTO> createMultipleWithoutError(@RequestBody List<SessionRequestDTO> dtos) {
        return sessionService.saveMultipleWithoutError(dtos);
    }
}
