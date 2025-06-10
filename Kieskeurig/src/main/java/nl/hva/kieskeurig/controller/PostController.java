package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.Post;
import nl.hva.kieskeurig.model.PostRequest;
import nl.hva.kieskeurig.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/api/posts")
    public ResponseEntity<Post> createPost(@RequestBody PostRequest post) {
        System.out.println("Received: " + post.toString());
        System.out.println(post.getTitle());
        Post savepost = postService.create(post);
        return ResponseEntity.ok(savepost);
    }
}
