package com.example.owasp.a09;

import com.example.owasp.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/a09")
public class A09LoggingController {
    private static final Logger log = LoggerFactory.getLogger(A09LoggingController.class);

    // INSECURE: logs PII/secrets
    @PostMapping("/insecure/login")
    public Message insecureLogin(@RequestParam String username, @RequestParam String password) {
        log.info("User login attempt: {} with password {}", username, password); // BAD
        return new Message("ok");
    }

    // SECURE: parameterized, no secrets
    @PostMapping("/secure/login")
    public Message secureLogin(@RequestParam String username, @RequestParam String password) {
        log.info("User login attempt: {}", username);
        return new Message("ok");
    }
}
