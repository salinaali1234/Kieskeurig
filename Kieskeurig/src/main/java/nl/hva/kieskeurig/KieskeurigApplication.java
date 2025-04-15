package nl.hva.kieskeurig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

@SpringBootApplication
public class KieskeurigApplication {

    public static void main(String[] args) throws IOException, XMLStreamException {
        SpringApplication.run(KieskeurigApplication.class, args);
    }
}
