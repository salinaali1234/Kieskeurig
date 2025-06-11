package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.exception.BadRequestException;
import nl.hva.kieskeurig.model.Post;
import nl.hva.kieskeurig.model.PostRequest;
import nl.hva.kieskeurig.repository.PostRepo;
import nl.hva.kieskeurig.repository.PostsRepo;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Service
public class PostService {

    private final PostRepo postsRepo;

    public PostService(PostRepo postsRepo) {
        this.postsRepo = postsRepo;
    }



    public Post create(PostRequest postRequest) throws BadRequestException {
        if (postRequest == null) throw new BadRequestException("post can't be null");
        validateTitle(postRequest.getTitle());
        validateContent(postRequest.getContent());
        validateAuthor(postRequest.getAuthor());
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .authorId(postRequest.getAuthor())
                .build();
        return postsRepo.save(post);
    }

    public void validateTitle(String title) throws BadRequestException {
        if (title == null || title.trim().isEmpty()) throw new BadRequestException("title can't be null");
    }

    public void validateContent(String content) throws BadRequestException {
        if (content == null || content.trim().isEmpty()) throw new BadRequestException("content can't be null");
    }

    public void validateAuthor(int author) throws BadRequestException {
        if (author == 0 ) throw new BadRequestException("there has to be an author");
    }
    public List<Post> getAllPosts() {
        return postsRepo.findAll(); // this method is included by default
    }
}
