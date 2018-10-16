package ua.bu.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.bu.dto.UserWithoutPass;
import ua.bu.exeptions.UserAlreadyExistsExeption;
import ua.bu.models.User;
import ua.bu.service.UserRepositoryImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class MainRestController {
    @Autowired
    UserRepositoryImpl userService;//dao
    //json pars
    ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //to hash pass
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
// just for usability
    @GetMapping("/api/user")
    public ResponseEntity<String> all() throws JsonProcessingException {
        List<User> all = userService.findAll();
        return new ResponseEntity<String>( mapper.writeValueAsString(all),HttpStatus.OK);
    }

    @PostMapping("/api/user")
    public ResponseEntity<String> saveU(@RequestBody String userinp) throws IOException { //ResponseEntity for rule http response
// user : json -> User
        User user = mapper.readValue(userinp, User.class);
        //check in pass and username  is not missing
        if (!(user.getUserName().isEmpty()) & !(user.getPlainTextPassword().isEmpty())) {
            //set hashpass and id
            user.setHashedPassword(passwordEncoder.encode(user.getPlainTextPassword()));
            user.setId(UUID.randomUUID().toString());
            try { //try to create user
                User serviceUser = userService.createUser(user);
                //if created, then   User -> dto
                UserWithoutPass userWithoutPass = new UserWithoutPass(serviceUser);
                String uWPJson = mapper.writeValueAsString(userWithoutPass);

                return new ResponseEntity<String>(uWPJson, HttpStatus.OK);

                //if we cant create user
            } catch (UserAlreadyExistsExeption userAlreadyExistsExeption) {
                userAlreadyExistsExeption.printStackTrace();
                //prepare error rsponse body
                Map<String,String > errResponseBody = new HashMap<>();
                errResponseBody.put("code","USER_ALREADY_EXISTS");
                errResponseBody.put("description", "A user with the given username already exists");
                String jERBody = mapper.writeValueAsString(errResponseBody);

                return new ResponseEntity<String>( jERBody,HttpStatus.CONFLICT);
            }
        }
        //if incoming data is clearly incorrect
        return new ResponseEntity<String>("User data is unreadable!", HttpStatus.BAD_REQUEST);

    }
}
