package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.model.Comment;
import nl.hva.kieskeurig.model.Post;
import nl.hva.kieskeurig.dto.CommentRequest;
import nl.hva.kieskeurig.repository.CommentRepo;
import nl.hva.kieskeurig.repository.PostRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final PostRepo postRepo;

    public CommentService(CommentRepo commentRepo, PostRepo postRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
    }

    public Comment addComment(CommentRequest req) {
        Post post = postRepo.findById(req.getPostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Comment comment = new Comment();
        comment.setContent(req.getContent());
        comment.setAuthorId(req.getAuthor());
        comment.setPost(post);

        return commentRepo.save(comment);
    }

    public List<Comment> getCommentsForPost(Long postId) {
        return commentRepo.findByPostId(postId);
    }
}
