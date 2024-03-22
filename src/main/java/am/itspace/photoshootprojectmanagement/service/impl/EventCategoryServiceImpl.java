package am.itspace.photoshootprojectmanagement.service.impl;

import am.itspace.photoshootprojectmanagement.entity.EventCategory;
import am.itspace.photoshootprojectmanagement.exception.InvalidEventCategoryException;
import am.itspace.photoshootprojectmanagement.repository.EventCategoryRepository;
import am.itspace.photoshootprojectmanagement.service.EventCategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventCategoryServiceImpl implements EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;

    @Override
    public EventCategory save(EventCategory eventCategory) {
        return eventCategoryRepository.save(eventCategory);
    }

    @Override
    public List<EventCategory> findAll() {
        return eventCategoryRepository.findAll();
    }

    @Override
    public Optional<EventCategory> findById(int id) {
        Optional<EventCategory> eventCategoryOptional = eventCategoryRepository.findById(id);
        if (eventCategoryOptional.isEmpty()) {
            throw new EntityNotFoundException("eventCategory does not exists!");
        }

        return eventCategoryOptional;
    }

    @Override
    public EventCategory update(EventCategory eventCategory) {
        return eventCategoryRepository.save(eventCategory);
    }

    @Override
    public void deleteById(int id) {
        if (findById(id).isEmpty()) {
            throw new InvalidEventCategoryException("eventCategory does not exists!");
        }

        eventCategoryRepository.deleteById(id);
    }
}
