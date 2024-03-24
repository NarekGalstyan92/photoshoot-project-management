package am.itspace.photoshootprojectmanagement.repository;

import am.itspace.photoshootprojectmanagement.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findByUserId(int id, Pageable pageable);

}
