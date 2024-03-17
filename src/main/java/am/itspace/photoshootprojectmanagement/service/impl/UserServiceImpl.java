package am.itspace.photoshootprojectmanagement.service.impl;

import am.itspace.photoshootprojectmanagement.component.FileComponent;
import am.itspace.photoshootprojectmanagement.entity.Role;
import am.itspace.photoshootprojectmanagement.entity.User;
import am.itspace.photoshootprojectmanagement.exception.UserExistException;
import am.itspace.photoshootprojectmanagement.repository.UserRepository;
import am.itspace.photoshootprojectmanagement.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final FileComponent fileComponent;

    @Override
    public User save(User user, MultipartFile multipartFile) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

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
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> findById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if ((userOptional.isPresent() && userOptional.get().isDeleted()) || userOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return userOptional;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Page<User> findByIsDeleted(boolean deleted, Pageable pageable) {
        return userRepository.findByIsDeleted(deleted, pageable);
    }

    @Override
    public User update(User user, MultipartFile multipartFile) {
        User userById = userRepository.findById(user.getId()).get();

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
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("User does not exists!");
        }

        User user = userOptional.get();

        user.setDeleted(true);
        user.setActive(false);

        userRepository.save(user);
    }

    @Override
    public void deletePicture(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String avatarUrl = user.getAvatarUrl();

            if (avatarUrl != null && !user.getAvatarUrl().isEmpty()) {
                user.setAvatarUrl(null);
                userRepository.save(user);

                fileComponent.deletePicture(avatarUrl);
            }
        }
    }
}
