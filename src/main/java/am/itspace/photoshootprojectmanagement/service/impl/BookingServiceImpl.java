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

        if (validateBooking(booking)) {
            booking.setStatus(Status.PENDING);
            booking.setBookingDate(new Date());
            return bookingRepository.save(booking);
        }
        throw new InvalidBookingException("Booking validation failed");
    }

    private boolean validateBooking(Booking booking) {
        if (booking == null) {
            throw new InvalidBookingException("Booking is null");
        }

        if (booking.getUser().getId() == 0) {
            throw new InvalidBookingException("User ID was less than 0");
        }

        // Check if eventStartTime is greater than eventEndTime
        if (booking.getEventStartTime().after(booking.getEventEndTime())) {
            throw new InvalidBookingException("Event end time should be after start time");
        }
        return true;
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
        if (findById(id).isEmpty()) {
            throw new InvalidBookingException("Booking with " + id + " id does not exist!");
        }

        bookingRepository.deleteById(id);
    }
}
