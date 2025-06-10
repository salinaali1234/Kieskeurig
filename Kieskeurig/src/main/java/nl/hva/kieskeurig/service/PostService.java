package nl.hva.kieskeurig.service;

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



    public Post create(PostRequest postRequest) {
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .authorId(postRequest.getAuthor())
                .build();
        return postsRepo.save(post);
    }

    public List<Post> getAllPosts() {
        return postsRepo.findAll(); // this method is included by default
    }
}
