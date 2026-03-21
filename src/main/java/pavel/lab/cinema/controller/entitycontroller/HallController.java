package pavel.lab.cinema.controller.entitycontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pavel.lab.cinema.dto.defaultdto.HallDTO;
import pavel.lab.cinema.dto.requestdto.HallRequestDTO;
import pavel.lab.cinema.service.HallService;

import java.util.List;

@RestController
@RequestMapping("/halls")
@RequiredArgsConstructor
public class HallController {

    private final HallService hallService;

    @PostMapping("/post")
    public HallDTO create(@Valid @RequestBody HallRequestDTO dto) {
        return hallService.save(dto);
    }

    @GetMapping
    public List<HallDTO> findAll() {
        return hallService.findAll();
    }

    @GetMapping("/{id}")
    public HallDTO findById(@PathVariable Long id) {
        return hallService.findById(id);
    }

    @PutMapping("/update/{id}")
    public HallDTO update(@PathVariable Long id, @Valid @RequestBody HallRequestDTO dto) {
        return hallService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        hallService.delete(id);
    }
}