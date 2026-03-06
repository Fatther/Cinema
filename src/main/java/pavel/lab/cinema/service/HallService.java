package pavel.lab.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pavel.lab.cinema.dto.defaultdto.HallDTO;
import pavel.lab.cinema.dto.requestdto.HallRequestDTO;
import pavel.lab.cinema.entity.Hall;
import pavel.lab.cinema.mapper.HallMapper;
import pavel.lab.cinema.repository.HallRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HallService {

    private final HallRepository hallRepository;
    private final HallMapper hallMapper;

    @Transactional
    public List<HallDTO> findAll() {
        return hallRepository.findAll().stream()
                .map(hallMapper::toDto)
                .toList();
    }

    @Transactional
    public HallDTO findById(Long id) {
        return hallRepository.findById(id)
                .map(hallMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Hall not found"));
    }

    @Transactional
    public HallDTO save(HallRequestDTO dto) {
        Hall hall = hallMapper.toEntity(dto);
        return hallMapper.toDto(hallRepository.save(hall));
    }

    @Transactional
    public HallDTO update(Long id, HallRequestDTO dto) {
        Hall existingHall = hallRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hall not found"));
        existingHall.setName(dto.getName());
        existingHall.setPrice(dto.getPrice());
        existingHall.setSeatAmount(dto.getSeatAmount());
        return hallMapper.toDto(existingHall);
    }

    @Transactional
    public void delete(Long id) {
        hallRepository.deleteById(id);
    }
}