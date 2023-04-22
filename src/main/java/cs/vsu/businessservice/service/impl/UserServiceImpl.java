package cs.vsu.businessservice.service.impl;

import cs.vsu.businessservice.entity.User;
import cs.vsu.businessservice.exception.EntityNotFoundException;
import cs.vsu.businessservice.repo.UserRepo;
import cs.vsu.businessservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
