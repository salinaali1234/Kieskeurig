package nl.hva.kieskeurig.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Municipality {
    private String name;
    private Integer Id;
    private Integer IdConstituency;

}
