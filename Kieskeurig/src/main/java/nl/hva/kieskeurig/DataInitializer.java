package nl.hva.kieskeurig;

import lombok.AllArgsConstructor;
import nl.hva.kieskeurig.service.ProvinceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ProvinceService provinceService;

    /**
     * Runs when the back-end starts
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing data...");

        if (provinceService.getAllProvinces().isEmpty()) {
            provinceService.populateDatabase();
        }

        System.out.println("Initialization complete.");
    }
}