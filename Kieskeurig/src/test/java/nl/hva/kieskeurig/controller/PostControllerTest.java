package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.PostRequest;
import nl.hva.kieskeurig.service.PostService;
import nl.hva.kieskeurig.model.Post;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @Test
    void createPost_shouldReturnCreatedPost() throws BadRequestException {
        // Arrange
        PostRequest inputPost = new PostRequest( "Test Title", "Test Content", 0);

        Post expectedPost = new Post(1, "Test Title", "Test Content", 0, Collections.emptyList());

        when(postService.create(inputPost)).thenReturn(expectedPost);

        // Act
        Post actualPost = postController.createPost(inputPost).getBody();

        // Assert
        assertEquals(expectedPost, actualPost);
    }
}