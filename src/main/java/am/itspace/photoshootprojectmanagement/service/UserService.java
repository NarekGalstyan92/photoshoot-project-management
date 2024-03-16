package am.itspace.photoshootprojectmanagement.service;

import am.itspace.photoshootprojectmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

    User save(User user, MultipartFile multipartFile);

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);

    Page<User> findByIsDeleted(boolean b, Pageable pageable);

    User update(User user, MultipartFile multipartFile);

    void deleteById(int id);

    void deletePicture(int id);

}
