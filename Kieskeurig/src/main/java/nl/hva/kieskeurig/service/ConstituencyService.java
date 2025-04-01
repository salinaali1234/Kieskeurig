package nl.hva.kieskeurig.service;
import  nl.hva.kieskeurig.model.Constituency;
import nl.hva.kieskeurig.reader.ContituencyReader;
import nl.hva.kieskeurig.repository.ConstituencyRepo.ConstituencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConstituencyService {
    private final ConstituencyRepo repo;
    private final List<Constituency> constituencies = new ArrayList<Constituency>();

    public void add(Constituency constituency) {
        constituencies.add(constituency);
    }


    @Autowired
    public ConstituencyService(ConstituencyRepo repo) {this.repo = repo;}

    public List<Constituency> getAll() {return repo.findAll();}


    public boolean readConstituencies() throws XMLStreamException, IOException {
        ContituencyReader reader = new ContituencyReader(this);
        return reader.readResults();
    }
}
