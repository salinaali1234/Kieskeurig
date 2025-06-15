package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.dto.CommentRequest;
import nl.hva.kieskeurig.model.Comment;
import nl.hva.kieskeurig.repository.CommentRepo;
import nl.hva.kieskeurig.repository.PostRepo;
import nl.hva.kieskeurig.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentRepo commentRepo;
    private final PostRepo postRepo;

    private final CommentService commentService;

    public CommentController(CommentService commentService, CommentRepo commentRepo, PostRepo postRepo ) {
        this.commentService = commentService;
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
    }

    @PostMapping("/api/comments")
    public ResponseEntity<Comment> addComment(@RequestBody CommentRequest req) {
        Comment saved = commentService.addComment(req);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/api/comments/{postId}")
    public List<Comment> getCommentsForPost(@PathVariable Long postId) {
        return commentRepo.findByPostId(postId);
    }
}

