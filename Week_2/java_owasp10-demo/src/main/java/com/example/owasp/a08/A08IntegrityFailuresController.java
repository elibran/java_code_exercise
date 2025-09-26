package com.example.owasp.a08;

import com.example.owasp.dto.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@RequestMapping("/a08")
public class A08IntegrityFailuresController {

    // INSECURE: Java native deserialization of untrusted input (Base64)
    @PostMapping("/insecure/deserialize")
    public Message insecureDeserialize(@RequestParam String b64) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(java.util.Base64.getDecoder().decode(b64)))) {
            Object obj = ois.readObject();
            return new Message("ok: " + obj);
        } catch (Exception e) {
            return new Message("error: " + e.getMessage());
        }
    }

    // SECURE: JSON to safe DTO
    public record Note(String title, String body) {}
    @PostMapping("/secure/deserialize")
    public Message secureDeserialize(@RequestBody Note note) {
        return new Message("ok: " + note.title());
    }
}
