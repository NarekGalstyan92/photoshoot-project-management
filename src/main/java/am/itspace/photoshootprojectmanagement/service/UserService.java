package am.itspace.photoshootprojectmanagement.service;

import am.itspace.photoshootprojectmanagement.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user, MultipartFile multipartFile);

    List<User> findAll();

    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);

    User update(User user, MultipartFile multipartFile);

    void deleteById(int id);

    void deletePicture(int id);

    List<User> findByIsDeleted(boolean b);
}
