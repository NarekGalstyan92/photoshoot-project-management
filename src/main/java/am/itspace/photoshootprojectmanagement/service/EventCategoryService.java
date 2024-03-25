package am.itspace.photoshootprojectmanagement.service;

import am.itspace.photoshootprojectmanagement.entity.EventCategory;

import java.util.List;
import java.util.Optional;

public interface EventCategoryService {

    EventCategory save(EventCategory eventCategory);

    List<EventCategory> findAll();

    Optional<EventCategory> findById(int id);

    EventCategory update(EventCategory eventCategory);

    void deleteById(int id);
}
