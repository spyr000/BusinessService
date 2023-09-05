package cs.vsu.businessservice.user.service;

import cs.vsu.businessservice.user.entity.User;

public interface UserService {
    User getUser(String username);
}
