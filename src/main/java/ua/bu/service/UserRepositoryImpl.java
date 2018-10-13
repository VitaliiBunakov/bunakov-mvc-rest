package ua.bu.service;

import org.springframework.beans.factory.annotation.Autowired;
import ua.bu.exeptions.UserAlreadyExistsExeption;
import ua.bu.models.User;
import ua.bu.repository.UserDAO;

public class UserRepositoryImpl implements UserRepository {
    @Autowired
    UserDAO userDAO;

    @Override
    public User createUser(User user) throws UserAlreadyExistsExeption {

        return null;
    }
}
