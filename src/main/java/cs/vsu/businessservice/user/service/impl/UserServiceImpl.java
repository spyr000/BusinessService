package cs.vsu.businessservice.user.service.impl;

import cs.vsu.businessservice.user.entity.User;
import cs.vsu.businessservice.exceptionhandling.exception.EntityNotFoundException;
import cs.vsu.businessservice.user.repo.UserRepo;
import cs.vsu.businessservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    @Override
    public User getUser(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class));
    }
}
