package ua.bu.exeptions;

public class UserAlreadyExistsExeption extends Exception {
    public UserAlreadyExistsExeption(String username) {
        super("user already exists - " + username);
    }

}
