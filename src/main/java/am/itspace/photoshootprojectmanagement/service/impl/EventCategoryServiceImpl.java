package am.itspace.photoshootprojectmanagement.service.impl;

import am.itspace.photoshootprojectmanagement.entity.EventCategory;
import am.itspace.photoshootprojectmanagement.exception.InvalidBookingException;
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
        if (validateEventCategory(eventCategory)) {
            return eventCategoryRepository.save(eventCategory);
        }
        throw new InvalidBookingException("Booking validation failed");
    }

    private boolean validateEventCategory(EventCategory eventCategory) {
        if (eventCategory == null) {
            throw new InvalidEventCategoryException("EventCategory is null");
        }

        if (eventCategory.getName() == null || eventCategory.getName().isEmpty()) {
            throw new InvalidEventCategoryException("EventCategory name is null or empty");
        }

        if (eventCategory.getDescription() == null || eventCategory.getDescription().isEmpty()) {
            throw new InvalidEventCategoryException("EventCategory description is null or empty");
        }

        if (eventCategory.getStartingPrice() <= 0) {
            throw new InvalidEventCategoryException("Starting price must be greater than 0");
        }

        return true;
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
