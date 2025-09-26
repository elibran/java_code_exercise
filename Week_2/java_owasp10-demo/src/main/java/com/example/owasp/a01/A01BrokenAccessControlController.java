package com.example.owasp.a01;

import com.example.owasp.dto.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/a01")
public class A01BrokenAccessControlController {

    // INSECURE: returns admin data to anyone
    @GetMapping("/insecure/secret")
    public ResponseEntity<Message> insecureSecret() {
        return ResponseEntity.ok(new Message("ADMIN SECRET: salary=₹10,00,000"));
    }

    // SECURE: checks role header (demo only)
    @GetMapping("/secure/secret")
    public ResponseEntity<?> secureSecret(@RequestHeader(name = "X-Role", required = false) String role) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body(new Message("Forbidden"));
        }
        return ResponseEntity.ok(new Message("ADMIN SECRET: salary=₹10,00,000"));
    }
}
