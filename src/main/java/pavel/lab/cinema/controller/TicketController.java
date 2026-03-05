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
import pavel.lab.cinema.dto.defaultdto.TicketDTO;
import pavel.lab.cinema.dto.requestdto.TicketRequestDTO;
import pavel.lab.cinema.service.TicketService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/post")
    public TicketDTO create(@RequestBody TicketRequestDTO dto) {
        return ticketService.create(dto);
    }

    @GetMapping
    public List<TicketDTO> findAll() {
        return ticketService.findAll();
    }

    @GetMapping("/lazy/{id}")
    public TicketDTO findLazyById(@PathVariable Long id) {
        return ticketService.findLazyById(id);
    }

    @GetMapping("/{id}")
    public TicketDTO findGoodById(@PathVariable Long id) {
        return ticketService.findGoodById(id);
    }

    @PutMapping("/update/{id}")
    public TicketDTO update(@PathVariable Long id, @RequestBody TicketRequestDTO dto) {
        return ticketService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        ticketService.delete(id);
    }
}
