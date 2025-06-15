package nl.hva.kieskeurig.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
    private String content;
    private int author;
    private int postId;

}

