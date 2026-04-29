package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pavel.lab.cinema.dto.defaultdto.TicketDTO;
import pavel.lab.cinema.dto.page.PageResponse;
import pavel.lab.cinema.dto.requestdto.TicketRequestDTO;
import pavel.lab.cinema.entity.Hall;
import pavel.lab.cinema.entity.Session;
import pavel.lab.cinema.entity.Ticket;
import pavel.lab.cinema.entity.Visitor;
import pavel.lab.cinema.mapper.TicketMapper;
import pavel.lab.cinema.repository.SessionRepository;
import pavel.lab.cinema.repository.TicketRepository;
import pavel.lab.cinema.repository.VisitorRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock private TicketRepository ticketRepository;
    @Mock private TicketMapper ticketMapper;
    @Mock private SessionRepository sessionRepository;
    @Mock private VisitorRepository visitorRepository;

    @InjectMocks
    private TicketService ticketService;

    private Hall hall;
    private Session session;
    private Visitor visitor;
    private Ticket ticket;
    private TicketDTO ticketDTO;
    private TicketRequestDTO ticketRequestDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        hall = new Hall();
        hall.setId(1L);
        hall.setName("Зал 1");
        hall.setSeatAmount(100);

        session = new Session();
        session.setId(1L);
        session.setHall(hall);

        visitor = new Visitor();
        visitor.setId(1L);
        visitor.setName("Иван");

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setSeatNumber(5);
        ticket.setSession(session);
        ticket.setVisitor(visitor);

        ticketDTO = new TicketDTO();
        ticketDTO.setId(1L);

        ticketRequestDTO = new TicketRequestDTO();
        ticketRequestDTO.setSessionId(1L);
        ticketRequestDTO.setVisitorId(1L);
        ticketRequestDTO.setSeatNumber(5);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void create_throwsWhenSessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.create(ticketRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Сессия с ID 1 не найден(а)");

        verifyNoInteractions(ticketRepository, visitorRepository);
    }

    @Test
    void findAll_returnsPage_onCacheMiss() {
        Page<Ticket> page = new PageImpl<>(List.of(ticket), pageable, 1);
        when(ticketRepository.findAll(pageable)).thenReturn(page);
        when(ticketMapper.toDto(ticket)).thenReturn(ticketDTO);

        PageResponse<TicketDTO> result = ticketService.findAll(pageable);

        assertThat(result).isNotNull();
        verify(ticketRepository).findAll(pageable);
    }

    @Test
    void findAll_returnsCached_onSecondCall() {
        Page<Ticket> page = new PageImpl<>(List.of(ticket), pageable, 1);
        when(ticketRepository.findAll(pageable)).thenReturn(page);
        when(ticketMapper.toDto(ticket)).thenReturn(ticketDTO);

        ticketService.findAll(pageable);
        ticketService.findAll(pageable);

        verify(ticketRepository, times(1)).findAll(pageable);
    }
    @Test
    void findLazyById_returnsDto() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.toDto(ticket)).thenReturn(ticketDTO);

        assertThat(ticketService.findLazyById(1L)).isEqualTo(ticketDTO);
    }

    @Test
    void findLazyById_throwsWhenNotFound() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.findLazyById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Билет с ID 99 не найден(а)");
    }
    @Test
    void findGoodById_returnsDto() {
        when(ticketRepository.findGoodById(1L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.toDto(ticket)).thenReturn(ticketDTO);

        assertThat(ticketService.findGoodById(1L)).isEqualTo(ticketDTO);
    }

    @Test
    void findGoodById_throwsWhenNotFound() {
        when(ticketRepository.findGoodById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.findGoodById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Билет с ID 99 не найден(а)");
    }
    @Test
    void update_updatesAndReturnsDto() {
        when(ticketRepository.findGoodById(1L)).thenReturn(Optional.of(ticket));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(ticketMapper.toDto(ticket)).thenReturn(ticketDTO);

        TicketDTO result = ticketService.update(1L, ticketRequestDTO);

        assertThat(result).isEqualTo(ticketDTO);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void update_throwsWhenTicketNotFound() {
        when(ticketRepository.findGoodById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.update(99L, ticketRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Билет с ID 99 не найден(а)");

        verify(ticketRepository, never()).save(any());
    }

    @Test
    void update_throwsWhenSessionNotFound() {
        when(ticketRepository.findGoodById(1L)).thenReturn(Optional.of(ticket));
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.update(1L, ticketRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Сессия с ID 1 не найден(а)");

        verify(ticketRepository, never()).save(any());
    }

    @Test
    void update_throwsWhenSeatNumberExceedsCapacity() {
        ticketRequestDTO.setSeatNumber(200);
        when(ticketRepository.findGoodById(1L)).thenReturn(Optional.of(ticket));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> ticketService.update(1L, ticketRequestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(": в зале Зал 1 недостаточно мест");

        verify(ticketRepository, never()).save(any());
    }

    @Test
    void update_throwsWhenVisitorNotFound() {
        when(ticketRepository.findGoodById(1L)).thenReturn(Optional.of(ticket));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(visitorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.update(1L, ticketRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Посетитель с ID 1 не найден(а)");

        verify(ticketRepository, never()).save(any());
    }
    @Test
    void delete_removesTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        ticketService.delete(1L);

        verify(ticketRepository).delete(ticket);
    }

    @Test
    void delete_throwsWhenNotFound() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Билет с ID 99 не найден(а)");

        verify(ticketRepository, never()).delete(any());
    }
    @Test
    void findTicketsByVisitor_returnsPage_onCacheMiss() {
        Page<Ticket> page = new PageImpl<>(List.of(ticket), pageable, 1);
        when(ticketRepository.findTicketsByVisitor("Иван", pageable)).thenReturn(page);
        when(ticketMapper.toDto(ticket)).thenReturn(ticketDTO);

        PageResponse<TicketDTO> result = ticketService.findTicketsByVisitor("Иван", pageable);

        assertThat(result).isNotNull();
        verify(ticketRepository).findTicketsByVisitor("Иван", pageable);
    }

    @Test
    void findTicketsByVisitor_returnsCached_onSecondCall() {
        Page<Ticket> page = new PageImpl<>(List.of(ticket), pageable, 1);
        when(ticketRepository.findTicketsByVisitor("Иван", pageable)).thenReturn(page);
        when(ticketMapper.toDto(ticket)).thenReturn(ticketDTO);

        ticketService.findTicketsByVisitor("Иван", pageable);
        ticketService.findTicketsByVisitor("Иван", pageable);

        verify(ticketRepository, times(1)).findTicketsByVisitor("Иван", pageable);
    }
    @Test
    void findTicketsByVisitorJPQL_returnsPage_onCacheMiss() {
        Page<Ticket> page = new PageImpl<>(List.of(ticket), pageable, 1);
        when(ticketRepository.findTicketsByVisitorJPQL("Иван", pageable)).thenReturn(page);
        when(ticketMapper.toDto(ticket)).thenReturn(ticketDTO);

        PageResponse<TicketDTO> result = ticketService.findTicketsByVisitorJPQL("Иван", pageable);

        assertThat(result).isNotNull();
        verify(ticketRepository).findTicketsByVisitorJPQL("Иван", pageable);
    }

    @Test
    void findTicketsByVisitorJPQL_returnsCached_onSecondCall() {
        Page<Ticket> page = new PageImpl<>(List.of(ticket), pageable, 1);
        when(ticketRepository.findTicketsByVisitorJPQL("Иван", pageable)).thenReturn(page);
        when(ticketMapper.toDto(ticket)).thenReturn(ticketDTO);

        ticketService.findTicketsByVisitorJPQL("Иван", pageable);
        ticketService.findTicketsByVisitorJPQL("Иван", pageable);

        verify(ticketRepository, times(1)).findTicketsByVisitorJPQL("Иван", pageable);
    }
}