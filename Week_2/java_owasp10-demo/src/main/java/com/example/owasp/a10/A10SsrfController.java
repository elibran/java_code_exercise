package com.example.owasp.a10;

import com.example.owasp.dto.Message;
import org.springframework.web.bind.annotation.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@RestController
@RequestMapping("/a10")
public class A10SsrfController {
    private static final Set<String> ALLOW = Set.of("example.com","httpbin.org");

    // INSECURE: fetch any URL (DO NOT DO THIS)
    @GetMapping("/insecure/fetch")
    public Message insecureFetch(@RequestParam String url) {
        return new Message(httpGet(url));
    }

    // SECURE: allow-list and block private ranges
    @GetMapping("/secure/fetch")
    public Message secureFetch(@RequestParam String url) {
        try {
            URL u = new URL(url);
            String host = u.getHost().toLowerCase();
            if (!ALLOW.contains(host)) return new Message("Blocked host");
            InetAddress addr = InetAddress.getByName(host);
            if (addr.isSiteLocalAddress() || addr.isAnyLocalAddress() || addr.isLoopbackAddress())
                return new Message("Blocked private address");
            return new Message(httpGet(url));
        } catch (Exception e) {
            return new Message("error: " + e.getMessage());
        }
    }

    private String httpGet(String url) {
        try (InputStream is = new URL(url).openStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8).substring(0, Math.min(200, 4096));
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
