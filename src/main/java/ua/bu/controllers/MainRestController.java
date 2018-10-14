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
    UserRepositoryImpl userService;
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/api/user")
    public String all() throws JsonProcessingException {
        List<User> all = userService.findAll();
        return mapper.writeValueAsString(all);
    }

    @PostMapping("/api/user")
    public ResponseEntity<String> saveU(@RequestBody String userinp) throws IOException {
        User user = mapper.readValue(userinp, User.class);
        if (!(user.getUserName().equals("")) & !(user.getPlainTextPassword().equals(""))) {
            user.setHashedPassword(passwordEncoder.encode(user.getPlainTextPassword()));
            user.setId(UUID.randomUUID().toString());
            try {
                User serviceUser = userService.createUser(user);
                UserWithoutPass userWithoutPass = new UserWithoutPass(serviceUser);
                String uWPJson = mapper.writeValueAsString(userWithoutPass);
                return new ResponseEntity<String>(uWPJson, HttpStatus.OK);

            } catch (UserAlreadyExistsExeption userAlreadyExistsExeption) {
                userAlreadyExistsExeption.printStackTrace();
                Map<String,String > errResponseBody = new HashMap<>();
                errResponseBody.put("code","USER_ALREADY_EXISTS");
                errResponseBody.put("description", "A user with the given username already exists");
                String jERBody = mapper.writeValueAsString(errResponseBody);
                return new ResponseEntity<String>( jERBody,HttpStatus.CONFLICT);
            }
        }

        return new ResponseEntity<String>("User data is unreadable!", HttpStatus.BAD_REQUEST);

    }
}
