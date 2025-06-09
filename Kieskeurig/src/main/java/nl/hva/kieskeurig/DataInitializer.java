package nl.hva.kieskeurig;

import lombok.AllArgsConstructor;
import nl.hva.kieskeurig.service.ConstituencyService;
import nl.hva.kieskeurig.service.MunicipalityService;
import nl.hva.kieskeurig.service.ProvinceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ProvinceService provinceService;
    private final ConstituencyService constituencyService;
    private final MunicipalityService municipalityService;

    /**
     * Runs when the back-end starts
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
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