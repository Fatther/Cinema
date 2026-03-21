package pavel.lab.cinema.controller.entitycontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pavel.lab.cinema.dto.defaultdto.TicketDTO;
import pavel.lab.cinema.dto.page.PageResponse;
import pavel.lab.cinema.dto.requestdto.TicketRequestDTO;
import pavel.lab.cinema.service.TicketService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/post")
    public TicketDTO create(@Valid @RequestBody TicketRequestDTO dto) {
        return ticketService.create(dto);
    }

    @GetMapping
    public PageResponse<TicketDTO> findAll(@PageableDefault(size = 3) Pageable pageable) {
        return ticketService.findAll(pageable);
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
    public TicketDTO update(@PathVariable Long id, @Valid @RequestBody TicketRequestDTO dto) {
        return ticketService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        ticketService.delete(id);
    }

    @GetMapping("/search")
    public PageResponse<TicketDTO> findTicketsByVisitor(@RequestParam("visitorName") String name,
                                                        @PageableDefault(size = 3) Pageable pageable) {
        return ticketService.findTicketsByVisitor(name, pageable);
    }

    @GetMapping("/jpqlsearch")
    public PageResponse<TicketDTO> findTicketsByVisitorJPQL(@RequestParam("visitorName") String name,
                                                    @PageableDefault(size = 3) Pageable pageable) {
        return ticketService.findTicketsByVisitorJPQL(name, pageable);
    }
}
