package nl.hva.kieskeurig.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO (Data Transfer Object) used for receiving post data from clients.
 * This class is typically used in HTTP request bodies when creating a new post.
 */
@Getter
@Setter
@AllArgsConstructor
public class PostRequest {

    /**
     * The title of the post.
     */
    private String title;

    /**
     * The content/body of the post.
     */
    private String content;

    /**
     * The ID of the author creating the post.
     */
    private int author;
}