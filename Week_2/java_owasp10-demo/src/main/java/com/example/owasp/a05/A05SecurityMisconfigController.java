package com.example.owasp.a05;

import com.example.owasp.dto.Message;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/a05")
public class A05SecurityMisconfigController {

    // INSECURE: exposes internal config
    @GetMapping("/insecure/config")
    public Message insecureConfig() {
        String secret = System.getenv().getOrDefault("DEMO_SECRET", "not_set");
        return new Message("DEMO_SECRET=" + secret);
    }

    // SECURE: redacts secrets
    @GetMapping("/secure/config")
    public Message secureConfig() {
        String secret = System.getenv().getOrDefault("DEMO_SECRET", "not_set");
        String redacted = secret.length() <= 4 ? "****" : secret.substring(0,2) + "****";
        return new Message("DEMO_SECRET=" + redacted);
    }
}
