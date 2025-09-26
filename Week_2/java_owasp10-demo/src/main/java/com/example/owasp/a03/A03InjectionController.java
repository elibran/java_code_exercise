package com.example.owasp.a03;

import com.example.owasp.dto.Message;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.*;

@RestController
@RequestMapping("/a03")
public class A03InjectionController {
    private final Map<String, String> users = Map.of("alice","1234","bob","abcd");

    // INSECURE: pretend "query" param is a SQL snippet; we 'concatenate' and eval via regex (simulated)
    @GetMapping("/insecure/find")
    public Message insecureFind(@RequestParam String username) {
        // naive 'contains' acting as injection vector: username like "alice' OR '1'='1"
        if (username.contains("' OR '1'='1")) {
            return new Message("All users: " + users.keySet());
        }
        if (users.containsKey(username)) {
            return new Message("Found: " + username);
        }
        return new Message("Not found");
    }

    // SECURE: strict allow-list on username
    @GetMapping("/secure/find")
    public Message secureFind(@RequestParam String username) {
        if (!username.matches("^[a-z0-9]{1,12}$")) {
            return new Message("Invalid username");
        }
        return users.containsKey(username) ? new Message("Found: " + username) : new Message("Not found");
    }
}
