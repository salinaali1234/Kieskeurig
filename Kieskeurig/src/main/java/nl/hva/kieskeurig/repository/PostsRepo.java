package nl.hva.kieskeurig.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Getter
@Setter

public class PostsRepo {
    public TreeMap<Integer, String> Post = new TreeMap<Integer, String>();
    private static Integer ID = 1;
    private static String Title = "fake Post";
//
//    public fakaPost(){
//        return Post.put(ID++, Title);
//    }
}
