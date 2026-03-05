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
import pavel.lab.cinema.dto.defaultdto.VisitorDTO;
import pavel.lab.cinema.dto.requestdto.VisitorRequestDTO;
import pavel.lab.cinema.service.VisitorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitors")
public class VisitorController {

    private final VisitorService visitorService;

    @PostMapping("/post")
    public VisitorDTO create(@RequestBody VisitorRequestDTO dto) {
        return visitorService.create(dto);
    }

    @GetMapping
    public List<VisitorDTO> findAll() {
        return visitorService.findAll();
    }

    @PutMapping("/update/{id}")
    public VisitorDTO update(@PathVariable Long id, @RequestBody VisitorRequestDTO dto) {
        return visitorService.update(id, dto);
    }

    @GetMapping("{id}")
    public VisitorDTO findById(@PathVariable Long id) {
        return visitorService.findById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        visitorService.delete(id);
    }
}
