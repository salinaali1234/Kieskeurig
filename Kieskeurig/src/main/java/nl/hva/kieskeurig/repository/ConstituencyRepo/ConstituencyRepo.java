package nl.hva.kieskeurig.repository.ConstituencyRepo;
import nl.hva.kieskeurig.model.Constituency;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ConstituencyRepo {
    private final List<Constituency> constituencies = new ArrayList<>();
    private static String id= "";


    public List<Constituency> findAll() {return constituencies;}

    public Constituency save(Constituency constituency) {
        constituencies.add(constituency);
        return constituency; // created task
    }

}
