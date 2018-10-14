package ua.bu.service;

import ua.bu.exeptions.UserAlreadyExistsExeption;
import ua.bu.models.User;

import java.util.List;

public interface UserRepository {
    User createUser(User user) throws UserAlreadyExistsExeption;

    int countByUserName(String username);
    List<User> findAll();


}
