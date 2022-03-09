package com.springboot.email.user;

import com.springboot.email.mail.EmailService;
import freemarker.template.TemplateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the Controller class for {@link User}.
 *
 * @author Papa Attrams
 * @since 1.0
 */

@RestController
@RequestMapping(path = "/accounts")
public class UserController {
    private final EmailService emailService;

    public UserController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) throws MessagingException, TemplateException, IOException {
        emailService.sendEmail("Sample Application Registration", user);

        Map<String, String> message = new HashMap<>();
        message.put("message", String.format("Registration Successfully. %s, check your email", user.getUsername()));

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}
