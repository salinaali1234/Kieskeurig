package nl.hva.kieskeurig.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
public class PostController {
    @PostMapping("/api/posts")
    public ResponseEntity<?> createPost(@RequestBody Map<String, String> data) {
        System.out.println("Received: " + data.toString());
        System.out.println(data.get("title"));
        return ResponseEntity.ok(Map.of("message", "Received"));
    }
}
