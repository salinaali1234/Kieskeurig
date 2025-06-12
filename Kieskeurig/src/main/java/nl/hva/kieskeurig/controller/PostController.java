package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.Post;
import nl.hva.kieskeurig.model.PostRequest;
import nl.hva.kieskeurig.service.PostService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller responsible for handling HTTP requests related to forum posts.
 * Provides endpoints to create new posts and fetch all existing posts.
 */
@RestController
public class PostController {

    private final PostService postService;

    /**
     * Constructor for PostController.
     *
     * @param postService the service layer used to process post-related logic
     */
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Endpoint to create a new post.
     *
     * @param post the request body containing title, content, and author ID
     * @return ResponseEntity containing the created Post object
     * @throws BadRequestException if validation or saving fails
     *
     * Example request:
     * POST /api/posts/create
     * {
     *   "title": "My Question",
     *   "content": "Can someone explain Spring Boot?",
     *   "author": 1
     * }
     */
    @PostMapping("/api/posts/create")
    public ResponseEntity<Post> createPost(@RequestBody PostRequest post) throws BadRequestException {
        System.out.println("Received: " + post.toString());
        System.out.println(post.getTitle());
        Post savepost = postService.create(post);
        return ResponseEntity.ok(savepost);
    }

    /**
     * Endpoint to fetch all forum posts.
     *
     * @return ResponseEntity containing a list of all Post objects
     *
     * Example request:
     * GET /api/posts
     */
    @GetMapping("/api/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
}
