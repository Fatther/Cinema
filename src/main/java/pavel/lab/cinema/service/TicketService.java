package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pavel.lab.cinema.cache.CacheKey;
import pavel.lab.cinema.dto.defaultdto.TicketDTO;
import pavel.lab.cinema.dto.page.PageResponse;
import pavel.lab.cinema.dto.requestdto.TicketRequestDTO;
import pavel.lab.cinema.entity.Session;
import pavel.lab.cinema.entity.Ticket;
import pavel.lab.cinema.entity.Visitor;
import pavel.lab.cinema.mapper.TicketMapper;
import pavel.lab.cinema.repository.SessionRepository;
import pavel.lab.cinema.repository.TicketRepository;
import pavel.lab.cinema.repository.VisitorRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final SessionRepository sessionRepository;
    private final VisitorRepository visitorRepository;
    private final Map<CacheKey, PageResponse<TicketDTO>> ticketCache = new HashMap<>();

    private static final String NOT_FOUND_MSG = " не найден(а)";
    private static final String TICKET_PREFIX = "Билет с ID ";
    private static final String SESSION_PREFIX = "Сессия с ID ";
    private static final String VISITOR_PREFIX = "Посетитель с ID ";

    @Transactional
    public TicketDTO create(TicketRequestDTO dto) {
        Session session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new EntityNotFoundException(SESSION_PREFIX + dto.getSessionId() + NOT_FOUND_MSG));
        if (dto.getSeatNumber() > session.getHall().getSeatAmount()) {
            throw new IllegalArgumentException(": в зале " + session.getHall().getName()
                    + " недостаточно мест (макс. " + session.getHall().getSeatAmount() + ")");
        }
        List<Integer> bookedSeats = session.getTickets().stream()
                .map(Ticket::getSeatNumber)
                .toList();
        if (bookedSeats.contains(dto.getSeatNumber())) {
            throw new IllegalArgumentException(": место под номером " + dto.getSeatNumber() + " занято");
        }
        Ticket ticket = ticketMapper.toEntity(dto);
        ticket.setSession(session);
        Visitor visitor = visitorRepository.findById(dto.getVisitorId())
                .orElseThrow(() -> new EntityNotFoundException(VISITOR_PREFIX + dto.getVisitorId() + NOT_FOUND_MSG));
        ticket.setVisitor(visitor);
        Ticket savedTicket = ticketRepository.save(ticket);
        ticketCache.clear();
        log.info("Билет на сессию с ID " + dto.getSessionId() + " создан");
        return ticketMapper.toDto(savedTicket);
    }

    @Transactional
    public PageResponse<TicketDTO> findAll(Pageable pageable) {
        CacheKey key = new CacheKey(pageable);
        if (ticketCache.containsKey(key)) {
            return ticketCache.get(key);
        }
        Page<Ticket> ticketsPage = ticketRepository.findAll(pageable);
        PageResponse<TicketDTO> dtosPage = new PageResponse<>(ticketsPage.map(ticketMapper::toDto));
        ticketCache.put(key, dtosPage);
        return dtosPage;
    }

    @Transactional
    public TicketDTO findLazyById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TICKET_PREFIX + id + NOT_FOUND_MSG));
        return ticketMapper.toDto(ticket);
    }

    @Transactional
    public TicketDTO findGoodById(Long id) {
        Ticket ticket = ticketRepository.findGoodById(id)
                .orElseThrow(() -> new EntityNotFoundException(TICKET_PREFIX + id + NOT_FOUND_MSG));
        return ticketMapper.toDto(ticket);
    }

    @Transactional
    public TicketDTO update(Long id, TicketRequestDTO dto) {
        Ticket ticket = ticketRepository.findGoodById(id)
                .orElseThrow(() -> new EntityNotFoundException(TICKET_PREFIX + id + NOT_FOUND_MSG));
        ticket.setSeatNumber(dto.getSeatNumber());
        Session session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new EntityNotFoundException(SESSION_PREFIX + dto.getSessionId() + NOT_FOUND_MSG));
        if (dto.getSeatNumber() > session.getHall().getSeatAmount()) {
            throw new IllegalArgumentException(": в зале " + session.getHall().getName() + " недостаточно мест");
        }
        ticket.setSession(session);
        Visitor visitor = visitorRepository.findById(dto.getVisitorId())
                .orElseThrow(() -> new EntityNotFoundException(VISITOR_PREFIX + dto.getVisitorId() + NOT_FOUND_MSG));
        ticket.setVisitor(visitor);
        Ticket updatedTicket = ticketRepository.save(ticket);
        ticketCache.clear();
        log.info(TICKET_PREFIX + id + " обновлён");
        return ticketMapper.toDto(updatedTicket);
    }

    @Transactional
    public void delete(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TICKET_PREFIX + id + NOT_FOUND_MSG));
        ticketRepository.delete(ticket);
        ticketCache.clear();
        log.info(TICKET_PREFIX + id + " удалён");
    }

    @Transactional
    public PageResponse<TicketDTO> findTicketsByVisitor(String name, Pageable pageable) {
        CacheKey key = new CacheKey(pageable, name);
        if (ticketCache.containsKey(key)) {
            return ticketCache.get(key);
        }
        Page<Ticket> ticketsPage = ticketRepository.findTicketsByVisitor(name, pageable);
        PageResponse<TicketDTO> dtosPage = new PageResponse<>(ticketsPage.map(ticketMapper::toDto));
        ticketCache.put(key, dtosPage);
        return dtosPage;
    }

    @Transactional
    public PageResponse<TicketDTO> findTicketsByVisitorJPQL(String name, Pageable pageable) {
        CacheKey key = new CacheKey(pageable, name);
        if (ticketCache.containsKey(key)) {
            return ticketCache.get(key);
        }
        Page<Ticket> ticketsPage = ticketRepository.findTicketsByVisitorJPQL(name, pageable);
        PageResponse<TicketDTO> dtosPage = new PageResponse<>(ticketsPage.map(ticketMapper::toDto));
        ticketCache.put(key, dtosPage);
        return dtosPage;
    }
}
