package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.exception.BadRequestException;
import nl.hva.kieskeurig.model.Post;
import nl.hva.kieskeurig.model.PostRequest;
import nl.hva.kieskeurig.repository.PostRepo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that contains the business logic related to forum posts.
 * Responsible for creating new posts, validating post data, and retrieving posts.
 */
@Service
public class PostService {

    private final PostRepo postsRepo;

    /**
     * Constructs a PostService with the given PostRepo.
     *
     * @param postsRepo the repository used for accessing Post data
     */
    public PostService(PostRepo postsRepo) {
        this.postsRepo = postsRepo;
    }

    /**
     * Creates and saves a new Post after validating the input data.
     *
     * @param postRequest the input containing title, content, and author ID
     * @return the saved Post entity
     * @throws BadRequestException if any field is invalid
     */
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

    /**
     * Validates the post title.
     *
     * @param title the title to validate
     * @throws BadRequestException if the title is null or empty
     */
    public void validateTitle(String title) throws BadRequestException {
        if (title == null || title.trim().isEmpty()) {
            throw new BadRequestException("title can't be null");
        }
    }

    /**
     * Validates the post content.
     *
     * @param content the content to validate
     * @throws BadRequestException if the content is null or empty
     */
    public void validateContent(String content) throws BadRequestException {
        if (content == null || content.trim().isEmpty()) {
            throw new BadRequestException("content can't be null");
        }
    }

    /**
     * Validates the author's ID.
     *
     * @param author the ID of the author
     * @throws BadRequestException if the author ID is 0 (invalid)
     */
    public void validateAuthor(int author) throws BadRequestException {
        if (author == 0) {
            throw new BadRequestException("there has to be an author");
        }
    }

    /**
     * Retrieves all forum posts from the database.
     *
     * @return a list of all Post entities
     */
    public List<Post> getAllPosts() {
        return postsRepo.findAll();
    }
}