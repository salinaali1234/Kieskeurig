package nl.hva.kieskeurig.service;
import  nl.hva.kieskeurig.model.Constituency;
import nl.hva.kieskeurig.repository.ConstituencyRepo.ConstituencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConstituencyService {
    private final ConstituencyRepo repo;

    @Autowired
    public ConstituencyService(ConstituencyRepo repo) {this.repo = repo;}

    public List<Constituency> getAll() {return repo.findAll();}
}
