package pavel.lab.cinema.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pavel.lab.cinema.dto.defaultdto.VisitorDTO;
import pavel.lab.cinema.dto.requestdto.VisitorRequestDTO;
import pavel.lab.cinema.entity.Visitor;
import pavel.lab.cinema.mapper.VisitorMapper;
import pavel.lab.cinema.repository.VisitorRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final VisitorMapper visitorMapper;

    private static final String VISITOR_PREFIX = "Посетитель с ID";
    private static final String NOT_FOUND_MSG = " не найден";

    @Transactional
    public VisitorDTO create(VisitorRequestDTO dto) {
        Visitor visitor = visitorMapper.toEntity(dto);
        Visitor createdVisitor = visitorRepository.save(visitor);
        log.info("Посетитель " + visitor.getName() + " добавлен в базу");
        return visitorMapper.toDto(createdVisitor);
    }

    @Transactional
    public List<VisitorDTO> findAll() {
        List<Visitor> visitors = visitorRepository.findAll();
        return visitors.stream()
                .map(visitorMapper::toDto)
                .toList();
    }

    @Transactional
    public VisitorDTO update(Long id, VisitorRequestDTO dto) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(VISITOR_PREFIX + id + NOT_FOUND_MSG));
        visitor.setName(dto.getName());
        visitor.setEmail(dto.getEmail());
        Visitor updatedVisitor = visitorRepository.save(visitor);
        log.info(VISITOR_PREFIX + id + " обновлён");
        return visitorMapper.toDto(updatedVisitor);
    }

    @Transactional
    public VisitorDTO findById(Long id) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(VISITOR_PREFIX + id + NOT_FOUND_MSG));
        return visitorMapper.toDto(visitor);
    }

    @Transactional
    public void delete(Long id) {
        visitorRepository.deleteById(id);
        log.info(VISITOR_PREFIX + id + " удалён");
    }
}
