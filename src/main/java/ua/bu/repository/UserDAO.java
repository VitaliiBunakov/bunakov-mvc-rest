package ua.bu.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ua.bu.models.User;

import java.util.List;
@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    User save(User user);

    List<User> findAll();

    User findByUserName(String username);

    int countByUserName(String username);


}
