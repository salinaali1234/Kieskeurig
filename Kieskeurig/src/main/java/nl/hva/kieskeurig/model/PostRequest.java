package nl.hva.kieskeurig.model;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;


import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor

public class PostRequest {

    private String title;
    private String content;
    private int author;

    public PostRequest() {

    }
}
