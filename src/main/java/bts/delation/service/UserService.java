package bts.delation.service;

import bts.delation.exception.NotFoundException;
import bts.delation.model.User;
import bts.delation.model.UserRole;
import bts.delation.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;

    public List<User> getUsers() {
        return repo.findAll();
    }

    public User getByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User getById(String id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User save(User user) {
        return repo.save(user);
    }

    public boolean isUserAuthorized(String email) {
        return repo.existsByEmail(email);
    }

    public Set<User> findModers() {
        return repo.findAllByUserRoleIn(List.of(UserRole.MODER, UserRole.ADMIN));
    }
}
