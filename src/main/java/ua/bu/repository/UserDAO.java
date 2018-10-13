package ua.bu.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ua.bu.models.User;

import java.util.List;

public interface UserDAO extends JpaRepository<User, Integer> {
    User save(User user);

    List<User> findAll();

    User findByUserName(String username);




}
