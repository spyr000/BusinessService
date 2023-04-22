package cs.vsu.businessservice.service;

import cs.vsu.businessservice.entity.Project;
import cs.vsu.businessservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.Set;

public interface UserService {
    User getUser(String username);
}
