package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pavel.lab.cinema.dto.defaultdto.VisitorDTO;
import pavel.lab.cinema.dto.requestdto.VisitorRequestDTO;
import pavel.lab.cinema.entity.Visitor;
import pavel.lab.cinema.mapper.VisitorMapper;
import pavel.lab.cinema.repository.VisitorRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorServiceTest {

    @Mock
    private VisitorRepository visitorRepository;

    @Mock
    private VisitorMapper visitorMapper;

    @InjectMocks
    private VisitorService visitorService;

    private Visitor visitor;
    private VisitorDTO visitorDTO;
    private VisitorRequestDTO visitorRequestDTO;

    @BeforeEach
    void setUp() {
        visitor = new Visitor();
        visitor.setId(1L);
        visitor.setName("Иван");
        visitor.setEmail("ivan@example.com");

        visitorDTO = new VisitorDTO();
        visitorDTO.setId(1L);
        visitorDTO.setName("Иван");

        visitorRequestDTO = new VisitorRequestDTO();
        visitorRequestDTO.setName("Иван");
        visitorRequestDTO.setEmail("ivan@example.com");
    }
    @Test
    void create_savesVisitorAndReturnsDto() {
        when(visitorMapper.toEntity(visitorRequestDTO)).thenReturn(visitor);
        when(visitorRepository.save(visitor)).thenReturn(visitor);
        when(visitorMapper.toDto(visitor)).thenReturn(visitorDTO);

        VisitorDTO result = visitorService.create(visitorRequestDTO);

        assertThat(result).isEqualTo(visitorDTO);
        verify(visitorRepository).save(visitor);
        verify(visitorMapper).toDto(visitor);
    }
    @Test
    void findAll_returnsMappedList() {
        when(visitorRepository.findAll()).thenReturn(List.of(visitor));
        when(visitorMapper.toDto(visitor)).thenReturn(visitorDTO);

        List<VisitorDTO> result = visitorService.findAll();

        assertThat(result).containsExactly(visitorDTO);
    }

    @Test
    void findAll_returnsEmptyList() {
        when(visitorRepository.findAll()).thenReturn(List.of());

        assertThat(visitorService.findAll()).isEmpty();
        verifyNoInteractions(visitorMapper);
    }
    @Test
    void update_updatesFieldsAndReturnsDto() {
        VisitorRequestDTO updateReq = new VisitorRequestDTO();
        updateReq.setName("Пётр");
        updateReq.setEmail("petr@example.com");

        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(visitorRepository.save(visitor)).thenReturn(visitor);
        when(visitorMapper.toDto(visitor)).thenReturn(visitorDTO);

        VisitorDTO result = visitorService.update(1L, updateReq);

        assertThat(visitor.getName()).isEqualTo("Пётр");
        assertThat(visitor.getEmail()).isEqualTo("petr@example.com");
        assertThat(result).isEqualTo(visitorDTO);
        verify(visitorRepository).save(visitor);
    }

    @Test
    void update_throwsWhenNotFound() {
        when(visitorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> visitorService.update(99L, visitorRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Посетитель с ID 99 не найден");

        verify(visitorRepository, never()).save(any());
    }
    @Test
    void findById_returnsDto() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(visitorMapper.toDto(visitor)).thenReturn(visitorDTO);

        assertThat(visitorService.findById(1L)).isEqualTo(visitorDTO);
    }

    @Test
    void findById_throwsWhenNotFound() {
        when(visitorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> visitorService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Посетитель с ID 99 не найден");
    }
    @Test
    void delete_removesVisitor() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));

        visitorService.delete(1L);

        verify(visitorRepository).delete(visitor);
    }

    @Test
    void delete_throwsWhenNotFound() {
        when(visitorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> visitorService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Посетитель с ID 99 не найден");

        verify(visitorRepository, never()).delete(any());
    }
}