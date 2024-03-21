package am.itspace.photoshootprojectmanagement.service;

import am.itspace.photoshootprojectmanagement.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookingService {

    Booking save(Booking booking);

    Page<Booking> findAll(Pageable pageable);

    Optional<Booking> findById(int id);

    Booking update(Booking booking);

    void deleteById(int id);

}
