package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pavel.lab.cinema.dto.defaultdto.HallDTO;
import pavel.lab.cinema.dto.requestdto.HallRequestDTO;
import pavel.lab.cinema.entity.Hall;
import pavel.lab.cinema.mapper.HallMapper;
import pavel.lab.cinema.repository.HallRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HallServiceTest {

    @Mock
    private HallRepository hallRepository;

    @Mock
    private HallMapper hallMapper;

    @InjectMocks
    private HallService hallService;

    private Hall hall;
    private HallDTO hallDTO;
    private HallRequestDTO hallRequestDTO;

    @BeforeEach
    void setUp() {
        hall = new Hall();
        hall.setId(1L);
        hall.setName("Зал 1");
        hall.setPrice(500);
        hall.setSeatAmount(100);

        hallDTO = new HallDTO();
        hallDTO.setId(1L);
        hallDTO.setName("Зал 1");

        hallRequestDTO = new HallRequestDTO();
        hallRequestDTO.setName("Зал 1");
        hallRequestDTO.setPrice(500);
        hallRequestDTO.setSeatAmount(100);
    }
    @Test
    void findAll_returnsMappedList() {
        when(hallRepository.findAll()).thenReturn(List.of(hall));
        when(hallMapper.toDto(hall)).thenReturn(hallDTO);

        List<HallDTO> result = hallService.findAll();

        assertThat(result).containsExactly(hallDTO);
        verify(hallRepository).findAll();
        verify(hallMapper).toDto(hall);
    }

    @Test
    void findAll_returnsEmptyList_whenNoHalls() {
        when(hallRepository.findAll()).thenReturn(List.of());

        List<HallDTO> result = hallService.findAll();

        assertThat(result).isEmpty();
        verify(hallRepository).findAll();
        verifyNoInteractions(hallMapper);
    }
    @Test
    void findById_returnsDto_whenFound() {
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(hallMapper.toDto(hall)).thenReturn(hallDTO);

        HallDTO result = hallService.findById(1L);

        assertThat(result).isEqualTo(hallDTO);
        verify(hallRepository).findById(1L);
        verify(hallMapper).toDto(hall);
    }

    @Test
    void findById_throwsEntityNotFoundException_whenNotFound() {
        when(hallRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hallService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Зал с ID 99 не найден");

        verify(hallRepository).findById(99L);
        verifyNoInteractions(hallMapper);
    }
    @Test
    void save_persistsHallAndReturnsDto() {
        when(hallMapper.toEntity(hallRequestDTO)).thenReturn(hall);
        when(hallRepository.save(hall)).thenReturn(hall);
        when(hallMapper.toDto(hall)).thenReturn(hallDTO);

        HallDTO result = hallService.save(hallRequestDTO);

        assertThat(result).isEqualTo(hallDTO);
        verify(hallMapper).toEntity(hallRequestDTO);
        verify(hallRepository).save(hall);
        verify(hallMapper).toDto(hall);
    }
    @Test
    void update_updatesFieldsAndReturnsDto() {
        HallRequestDTO updateRequest = new HallRequestDTO();
        updateRequest.setName("Зал VIP");
        updateRequest.setPrice(1000);
        updateRequest.setSeatAmount(50);

        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(hallMapper.toDto(hall)).thenReturn(hallDTO);

        HallDTO result = hallService.update(1L, updateRequest);

        assertThat(result).isEqualTo(hallDTO);
        assertThat(hall.getName()).isEqualTo("Зал VIP");
        assertThat(hall.getPrice()).isEqualTo(1000);
        assertThat(hall.getSeatAmount()).isEqualTo(50);

        verify(hallRepository).findById(1L);
        verify(hallMapper).toDto(hall);
        verify(hallRepository, never()).save(any());
    }

    @Test
    void update_throwsEntityNotFoundException_whenNotFound() {
        when(hallRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hallService.update(99L, hallRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Зал с ID 99 не найден");

        verify(hallRepository).findById(99L);
        verifyNoInteractions(hallMapper);
    }
    @Test
    void delete_removesHall_whenFound() {
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));

        hallService.delete(1L);

        verify(hallRepository).findById(1L);
        verify(hallRepository).delete(hall);
    }

    @Test
    void delete_throwsEntityNotFoundException_whenNotFound() {
        when(hallRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hallService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Зал с ID 99 не найден");

        verify(hallRepository).findById(99L);
        verify(hallRepository, never()).delete(any());
    }
}