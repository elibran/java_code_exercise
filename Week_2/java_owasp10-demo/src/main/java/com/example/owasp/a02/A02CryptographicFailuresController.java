package com.example.owasp.a02;

import com.example.owasp.dto.Message;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/a02")
public class A02CryptographicFailuresController {

    // INSECURE: returns plaintext back
    @PostMapping("/insecure/store")
    public Message insecureStore(@RequestParam("password") String password) {
        return new Message("Stored: " + password); // Don't do this!
    }

    // SECURE: bcrypt hash
    @PostMapping("/secure/store")
    public Message secureStore(@RequestParam String password) {
        String hash = new BCryptPasswordEncoder().encode(password);
        return new Message(hash);
    }
}
