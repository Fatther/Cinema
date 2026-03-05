package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import pavel.lab.cinema.dto.defaultdto.TicketDTO;
import pavel.lab.cinema.dto.requestdto.TicketRequestDTO;
import pavel.lab.cinema.entity.Session;
import pavel.lab.cinema.entity.Ticket;
import pavel.lab.cinema.entity.Visitor;
import pavel.lab.cinema.mapper.TicketMapper;
import pavel.lab.cinema.repository.SessionRepository;
import pavel.lab.cinema.repository.TicketRepository;
import pavel.lab.cinema.repository.VisitorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final SessionRepository sessionRepository;
    private final VisitorRepository visitorRepository;

    @Transactional
    public TicketDTO create(TicketRequestDTO dto) {
        Ticket ticket = ticketMapper.toEntity(dto);
        Session session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new EntityNotFoundException("Session with id " + dto.getSessionId() + " not found"));
        ticket.setSession(session);
        Visitor visitor = visitorRepository.findById(dto.getVisitorId())
                .orElseThrow(() -> new EntityNotFoundException("Visitor with id " + dto.getVisitorId() + " not found"));
        ticket.setVisitor(visitor);
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDto(savedTicket);
    }

    @Transactional
    public List<TicketDTO> findAll() {
    List<Ticket> tickets = ticketRepository.findAll();
    return tickets.stream()
            .map(ticketMapper::toDto)
            .toList();
    }

    @Transactional
    public TicketDTO findLazyById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with id " + id + " not found"));
        return ticketMapper.toDto(ticket);
    }

    @Transactional
    public TicketDTO findGoodById(Long id) {
        Ticket ticket = ticketRepository.findGoodById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with id " + id + " not found"));
        return ticketMapper.toDto(ticket);
    }

    @Transactional
    public TicketDTO update(Long id, TicketRequestDTO dto) {
        Ticket ticket = ticketRepository.findGoodById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with id " + id + " not found"));
        ticket.setSeatNumber(dto.getSeatNumber());
        Session session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new EntityNotFoundException("Session with id " + dto.getSessionId() + " not found"));
        ticket.setSession(session);
        Visitor visitor = visitorRepository.findById(dto.getVisitorId())
                .orElseThrow(() -> new EntityNotFoundException("Visitor with id " + dto.getVisitorId() + " not found"));
        ticket.setVisitor(visitor);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDto(updatedTicket);
    }

    @Transactional
    public void delete(Long id) {
        ticketRepository.deleteById(id);
    }
}
