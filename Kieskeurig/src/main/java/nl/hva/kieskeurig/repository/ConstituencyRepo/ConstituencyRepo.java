package nl.hva.kieskeurig.repository.ConstituencyRepo;
import nl.hva.kieskeurig.model.Constituency;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ConstituencyRepo {
    private final List<Constituency> constituencies = new ArrayList<>();
    private static int generatedId = 0;

    public ConstituencyRepo() {
        var constiuancy1 = new Constituency(1, "Groningen");
        this.save(constiuancy1);
        var constiuancy2 = new Constituency(2, "Leeuwarden");
        this.save(constiuancy2);
        var constiuancy3 = new Constituency(3, " Assen");
        this.save(constiuancy3);
        var constiuancy4 = new Constituency(4, "Zwolle");
        this.save(constiuancy4);
        var constiuancy5 = new Constituency(5, " Lelystad");
        this.save(constiuancy5);
        var constiuancy6 = new Constituency(6, "Amsterdam");
        this.save(constiuancy6);
        var constiuancy7 = new Constituency(7, "Haarlem");
        this.save(constiuancy7);
        var constiuancy8 = new Constituency(8, "Den Haag");
        this.save(constiuancy8);
        var constiuancy9 = new Constituency(9, "Dordrecht");
        this.save(constiuancy9);
        var constiuancy10 = new Constituency(10, "Rotterdam");
        this.save(constiuancy10);
        var constiuancy11 = new Constituency(11, "Middelburg");
        this.save(constiuancy11);
        var constiuancy12 = new Constituency(12, "'s-Hertogenbosch");
        this.save(constiuancy12);
        var constiuancy13 = new Constituency(13, "Arnhem");
        this.save(constiuancy13);


    }

    public List<Constituency> findAll() {return constituencies;}

    public Constituency save(Constituency constituency) {
        constituency.setId(++generatedId);// automatically increment id on creation.
        constituencies.add(constituency);
        return constituency; // created task
    }


}
