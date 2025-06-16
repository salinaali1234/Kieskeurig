package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.exception.BadRequestException;
import nl.hva.kieskeurig.model.Post;
import nl.hva.kieskeurig.model.PostRequest;
import nl.hva.kieskeurig.repository.PostRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PostServiceTest {

    private PostRepo postRepo;
    private PostService postService;

    @BeforeEach
    void setUp() {
        postRepo = mock(PostRepo.class);
        postService = new PostService(postRepo);
    }

    @Test
    void testCreateValidPost() throws BadRequestException {
        PostRequest request = new PostRequest("Test Title", "Test Content", 1);

        Post expectedPost = Post.builder()
                .title("Test Title")
                .content("Test Content")
                .authorId(1)
                .build();

        when(postRepo.save(any(Post.class))).thenReturn(expectedPost);

        Post actualPost = postService.create(request);

        assertEquals(expectedPost.getTitle(), actualPost.getTitle());
        assertEquals(expectedPost.getContent(), actualPost.getContent());
        assertEquals(expectedPost.getAuthorId(), actualPost.getAuthorId());
        verify(postRepo, times(1)).save(any(Post.class));
    }

    @Test
    void testCreatePostWithNullTitleThrowsException() {
        PostRequest request = new PostRequest(null, "Test Content", 1);
        BadRequestException ex = assertThrows(BadRequestException.class, () -> postService.create(request));
        assertEquals("title can't be null", ex.getMessage());
    }

    @Test
    void testCreatePostWithEmptyContentThrowsException() {
        PostRequest request = new PostRequest("Title", "   ", 1);
        BadRequestException ex = assertThrows(BadRequestException.class, () -> postService.create(request));
        assertEquals("content can't be null", ex.getMessage());
    }

    @Test
    void testCreatePostWithAuthorZeroThrowsException() {
        PostRequest request = new PostRequest("Title", "Content", 0);
        BadRequestException ex = assertThrows(BadRequestException.class, () -> postService.create(request));
        assertEquals("there has to be an author", ex.getMessage());
    }

    @Test
    void testGetAllPostsReturnsList() {
        List<Post> mockPosts = Arrays.asList(
                Post.builder().title("A").content("B").authorId(1).build(),
                Post.builder().title("X").content("Y").authorId(2).build()
        );

        when(postRepo.findAll()).thenReturn(mockPosts);

        List<Post> result = postService.getAllPosts();

        assertEquals(2, result.size());
        verify(postRepo, times(1)).findAll();
    }
}