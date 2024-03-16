package am.itspace.photoshootprojectmanagement.service.impl;

import am.itspace.photoshootprojectmanagement.component.FileComponent;
import am.itspace.photoshootprojectmanagement.entity.Role;
import am.itspace.photoshootprojectmanagement.entity.User;
import am.itspace.photoshootprojectmanagement.exception.UserExistException;
import am.itspace.photoshootprojectmanagement.repository.UserRepository;
import am.itspace.photoshootprojectmanagement.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final FileComponent fileComponent;

    @Override
    public User save(User user, MultipartFile multipartFile) {
        Optional<User> userOptional = findByEmail(user.getEmail());

        if (userOptional.isPresent()) {
            User userOpt = userOptional.get();
            if (!userOpt.isDeleted()) {
                throw new UserExistException("User with email " +
                        user.getEmail() + " already exists!");
            } else {
                userOpt.setDeleted(false);
                userRepository.save(userOpt);

                return userOpt;
            }
        }

        user.setDeleted(false);
        user.setActive(false);
        user.setRegisterDate(new Date());
        user.setHasLeftReview(false);
        user.setRole(Role.USER);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            user.setAvatarUrl(fileComponent.uploadPicture(multipartFile));
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if ((userOptional.isPresent() && userOptional.get().isDeleted()) || userOptional.isEmpty()) {
            throw new EntityNotFoundException("User does not exists!");
        }

        return userOptional;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User update(User user, MultipartFile multipartFile) {
        User userById = findById(user.getId()).get();

        if (multipartFile != null && !multipartFile.isEmpty()) {
            user.setAvatarUrl(fileComponent.uploadPicture(multipartFile));
        } else {
            user.setAvatarUrl(userById.getAvatarUrl());
        }

        user.setRegisterDate(userById.getRegisterDate());
        user.setActive(userById.isActive());
        user.setHasLeftReview(userById.isHasLeftReview());
        user.setDeleted(userById.isDeleted());
        user.setRole(userById.getRole());

        return userRepository.save(user);
    }

    @Override
    public void deleteById(int id) {
        User user = findById(id).get();

        user.setDeleted(true);
        user.setActive(false);

        userRepository.save(user);
    }

    @Override
    public void deletePicture(int id) {
        User user = findById(id).get();
        String avatarUrl = user.getAvatarUrl();

        if (avatarUrl != null && !user.getAvatarUrl().isEmpty()) {
            user.setAvatarUrl(null);
            userRepository.save(user);

            fileComponent.deletePicture(avatarUrl);
        }
    }

    @Override
    public List<User> findByIsDeleted(boolean deleted) {
        return userRepository.findByIsDeleted(deleted);
    }
}
