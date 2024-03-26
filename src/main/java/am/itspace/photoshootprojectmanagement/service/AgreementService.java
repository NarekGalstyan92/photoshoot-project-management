package am.itspace.photoshootprojectmanagement.service;

import am.itspace.photoshootprojectmanagement.entity.Agreement;
import am.itspace.photoshootprojectmanagement.security.SpringUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AgreementService {

    Agreement save(int bookingId, Agreement agreement, SpringUser springUser);

    Page<Agreement> findAll(Pageable pageable, SpringUser springUser);

    Optional<Agreement> findById(int id, SpringUser springUser);

    Agreement update(Agreement agreement, SpringUser springUser);

    void deleteById(int id, SpringUser springUser);

}
