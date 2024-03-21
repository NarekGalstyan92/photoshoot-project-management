package am.itspace.photoshootprojectmanagement.repository;

import am.itspace.photoshootprojectmanagement.entity.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {

    List<EventCategory> findAll();

}
