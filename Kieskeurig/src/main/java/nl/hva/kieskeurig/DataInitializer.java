package nl.hva.kieskeurig;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {
    /**
     * Runs when the back-end starts
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
    }
}