package ua.bu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.bu.exeptions.UserAlreadyExistsExeption;
import ua.bu.models.User;
import ua.bu.repository.UserDAO;

import java.util.List;
@Service
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    UserDAO userDAO;
//to check is no one user with that name is in  base already
    @Override
    public int countByUserName(String username) {
        return userDAO.countByUserName(username);
    }

    @Override
    public List<User> findAll() {

        return userDAO.findAll();
    }

    @Override
    public User createUser(User user) throws UserAlreadyExistsExeption {
        if (userDAO.countByUserName(user.getUserName()) < 1) { //no users with  this username in base?
            userDAO.save(user);
            return user;
        } else {
            throw new UserAlreadyExistsExeption(user.getUserName());
        }
    }


}



