package am.itspace.photoshootprojectmanagement.service.impl;

import am.itspace.photoshootprojectmanagement.entity.Booking;
import am.itspace.photoshootprojectmanagement.entity.Status;
import am.itspace.photoshootprojectmanagement.exception.InvalidBookingException;
import am.itspace.photoshootprojectmanagement.repository.BookingRepository;
import am.itspace.photoshootprojectmanagement.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking save(Booking booking) {

        booking.setStatus(Status.PENDING);
        booking.setBookingDate(new Date());

        return bookingRepository.save(booking);

    }

    @Override
    public Page<Booking> findAll(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    @Override
    public Optional<Booking> findById(int id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isEmpty()) {
            throw new InvalidBookingException("Booking with " + id + " id does not exists!");
        }

        return bookingOptional;
    }

    @Override
    public Booking update(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteById(int id) {
        findById(id);
        bookingRepository.deleteById(id);
    }
}
