package ua.bu.controllers;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.bu.dto.UserWithoutPass;
import ua.bu.models.User;
import ua.bu.repository.UserDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class MainRestController {
    @Autowired
    UserDAO userService;
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/api/user")
    public List<User> all() {
        List<User> all = userService.findAll();
        return all;
    }

    @PostMapping("/api/user")
    public String saveU(@RequestBody String userinp) throws IOException {
        User user = mapper.readValue(userinp, User.class);
        ArrayList byUserName = new ArrayList<>();
        byUserName.add(userService.findByUserName(user.getUserName()));

        System.out.println(userinp);
        System.out.println(user);
        System.out.println((byUserName.isEmpty()));
        if (!user.getUserName().equals("") & !(byUserName.isEmpty())) {
            user.setId(UUID.randomUUID().toString());
            if (!user.getPlainTextPassword().equals("")) {
                user.setHashedPassword(passwordEncoder.encode(user.getPlainTextPassword()));
            }
            System.out.println(user);
            userService.save(user);
            System.out.println("saved  bitrth");
            UserWithoutPass userWithoutPass = new UserWithoutPass(user);
            System.out.println(userWithoutPass.toString());
            return mapper.writeValueAsString(userWithoutPass);
        } else {
            System.out.println("User Exist");
            return "dsd";
        }
    }


}
