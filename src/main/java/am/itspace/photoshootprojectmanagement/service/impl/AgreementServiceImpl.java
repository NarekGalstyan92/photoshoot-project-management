package am.itspace.photoshootprojectmanagement.service.impl;

import am.itspace.photoshootprojectmanagement.entity.Agreement;
import am.itspace.photoshootprojectmanagement.repository.AgreementRepository;
import am.itspace.photoshootprojectmanagement.security.SpringUser;
import am.itspace.photoshootprojectmanagement.service.AgreementService;
import am.itspace.photoshootprojectmanagement.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository agreementRepository;

    private final BookingService bookingService;

    @Override
    public Agreement save(int bookingId,
                          Agreement agreement,
                          SpringUser springUser) {

        log.info("User with email {} called save()",
                springUser.getUser().getEmail());

        agreement.setUser(springUser.getUser());
        agreement.setBooking(bookingService.findById(bookingId).get());

        Agreement savedAgreement = agreementRepository.save(agreement);

        log.info("Review was left successfully by User with email {}",
                springUser.getUser().getEmail());

        return savedAgreement;
    }

    @Override
    public Page<Agreement> findAll(Pageable pageable,
                                   SpringUser springUser) {

        log.info("User with email {} called findAll()",
                springUser.getUser().getEmail());

        return agreementRepository.findAll(pageable);
    }

    @Override
    public Optional<Agreement> findById(int id,
                                        SpringUser springUser) {
        Optional<Agreement> agreementOptional = agreementRepository.findById(id);

        log.info("User with email {} called findById()",
                springUser.getUser().getEmail());

        if (agreementOptional.isEmpty()) {
            log.error("Failed to find Agreement with id {}", id);

            throw new EntityNotFoundException("Agreement does not exists!");
        }

        return agreementOptional;
    }

    @Override
    public Agreement update(Agreement agreement,
                            SpringUser springUser) {

        log.info("User with email {} called update()",
                springUser.getUser().getEmail());

        agreement.setUser(springUser.getUser());

        Agreement agreementOpt = findById(agreement.getId(), springUser).get();
        agreement.setBooking(agreementOpt.getBooking());

        Agreement updatedAgreement = agreementRepository.save(agreement);

        log.info("Agreement with id {} updated successfully by User with email {}",
                agreement.getId(), springUser.getUser().getEmail());

        return updatedAgreement;
    }

    @Override
    public void deleteById(int id,
                           SpringUser springUser) {
        findById(id, springUser);

        log.info("User with email {} called deleteById()",
                springUser.getUser().getEmail());

        agreementRepository.deleteById(id);

        log.info("Agreement with id {} deleted successfully by User with email {}",
                id, springUser.getUser().getEmail());
    }
}
