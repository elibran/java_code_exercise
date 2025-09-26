package com.example.owasp.a04;

import com.example.owasp.dto.Message;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/a04")
public class A04InsecureDesignController {
    private final Map<String, Integer> counts = new ConcurrentHashMap<>();
    private final Map<String, Long> window = new ConcurrentHashMap<>();

    // INSECURE: no rate limit
    @GetMapping("/insecure/ping")
    public Message ping() { return new Message("pong"); }

    // SECURE: 5 req / 10s per ip
    @GetMapping("/secure/ping")
    public Message pingSecure(@RequestHeader(value="X-Forwarded-For", required=false) String xff,
                              @RequestHeader(value="X-Real-IP", required=false) String xrip) {
        String ip = xrip != null ? xrip : (xff != null ? xff.split(",")[0].trim() : "unknown");
        long now = Instant.now().getEpochSecond();
        window.putIfAbsent(ip, now);
        if (now - window.get(ip) > 10) { window.put(ip, now); counts.put(ip, 0); }
        counts.merge(ip, 1, Integer::sum);
        if (counts.get(ip) > 5) return new Message("Too Many Requests");
        return new Message("pong");
    }
}
