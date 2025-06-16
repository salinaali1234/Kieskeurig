package nl.hva.kieskeurig;

import nl.hva.kieskeurig.service.ConstituencyService;
import nl.hva.kieskeurig.service.MunicipalityService;
import nl.hva.kieskeurig.service.ProvinceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

@Component
public class DataInitializer implements CommandLineRunner {
    private final ProvinceService provinceService;
    private final ConstituencyService constituencyService;
    private final MunicipalityService municipalityService;

    // Makes sure that DataInitializer doesn't run when unit testing
    @Value("${app.populate-database-on-startup:true}")
    private boolean populateDatabaseOnStartup;

    public DataInitializer(ProvinceService provinceService, ConstituencyService constituencyService, MunicipalityService municipalityService) {
        this.provinceService = provinceService;
        this.constituencyService = constituencyService;
        this.municipalityService = municipalityService;
    }

    /**
     * Runs when the back-end starts
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        if (populateDatabaseOnStartup) {
            initializeData();
        }
    }

    private void initializeData() throws XMLStreamException, IOException {
        System.out.println("Initializing data...");
        System.out.println("=========================");

        if (provinceService.isEmpty()) {
            System.out.println("Initializing province data...");
            provinceService.populateDatabase();
            System.out.println("Initializing province data complete.");
            System.out.println("=========================");
        }

        if (constituencyService.isEmpty()) {
            System.out.println("Initializing constituency data...");
            constituencyService.connectElectionDefinition();
            System.out.println("Initializing constituency data complete.");
            System.out.println("=========================");
        }

        if (municipalityService.isEmpty()) {
            System.out.println("Initializing municipality data...");
            municipalityService.connectElectionDefinition();
            System.out.println("Initializing municipality data complete.");
            System.out.println("=========================");
        }

        System.out.println("Initialization complete.");
    }
}