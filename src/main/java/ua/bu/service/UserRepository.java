package ua.bu.service;

import ua.bu.exeptions.UserAlreadyExistsExeption;
import ua.bu.models.User;

public interface UserRepository {
    User createUser(User user) throws UserAlreadyExistsExeption;


}
